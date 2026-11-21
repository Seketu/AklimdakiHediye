package com.reylortechnology.aklimdakihediye.Services

import android.net.Uri
import android.util.Log
import com.reylortechnology.aklimdakihediye.models.Enums.MarketPlaces
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ImageCandidate
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ProductResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.jsoup.Jsoup
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.random.Random

class ImageSearchService @Inject constructor(
    private val client: HttpClient
) {
    // Hilt'te @Singleton sağlanıyor; bu yüzden ikisi de tüm uygulama için tek.
    // Aynı anda en fazla 3 istek: 10 paralel istek Bing'de captcha'ya düşürüyordu.
    private val requestLimiter = Semaphore(3)
    private val cache = ConcurrentHashMap<String, ProductResult>()

    private val USER_AGENTS = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
    )

    // Sorgudan atılacak kelimeler (alt string değil, tam kelime olarak)
    private val STOP_WORDS = setOf("fiyat", "fiyatı", "fiyatları", "tl", "₺", "ve", "ile", "için", "bir")

    private val ACCESSORY_WORDS = setOf("uyumlu", "kılıf", "kordon", "kayış", "yedek", "aparat", "koruyucu")

    // Sadece yol parçası olarak geçerse kötü say ("blanket", "silicone" gibi kelimeleri elemesin)
    private val BAD_IMAGE_REGEX =
        Regex("(^|[/_\\-.])(logo|icon|favicon|placeholder|sprite|banner|avatar|noimage|no-image)([/_\\-.]|$)")

    private val trLocale = Locale("tr")

    suspend fun searchProduct(query: String): ProductResult = withContext(Dispatchers.IO) {
        val tokens = tokenize(query)
        val cleanQuery = tokens.joinToString(" ")
        if (cleanQuery.isBlank()) return@withContext ProductResult(null, null)

        cache[cleanQuery]?.let { return@withContext it }

        Log.d("HybridSearch", "🔍 Aranıyor: $cleanQuery")

        // Market sonuçları puanlamada öne çıkarılıyor; Bing görsellerinde "site:" filtresi
        // neredeyse hiç market sonucu döndürmediği için kullanılmıyor.
        val candidate = pickBest(fetchCandidates(cleanQuery), tokens)?.first
        val productLink = candidate?.pageUrl?.takeIf { MarketPlaces.isMarketPlace(it) }
            ?: "https://www.trendyol.com/sr?q=${Uri.encode(cleanQuery)}"

        val image = candidate?.let { chooseDisplayImage(it) }
        if (image == null) Log.e("HybridSearch", "❌ Uygun görsel bulunamadı: $cleanQuery")

        val result = ProductResult(productLink, image)
        if (image != null) cache[cleanQuery] = result
        result
    }

    private suspend fun fetchCandidates(q: String): List<ImageCandidate> = requestLimiter.withPermit {
        try {
            delay(Random.nextLong(200, 600))
            // "/images/search" sayfası, çerezsiz isteklerde sorguyla alakasız sonuçlar döndürüyor.
            // "/images/async" uç noktası gerçek arama sonuçlarını veriyor.
            val url = "https://www.bing.com/images/async?q=${Uri.encode(q)}" +
                    "&first=0&count=35&mmasync=1&mkt=tr-TR&qft=+filterui:imagesize-medium"

            val response = client.get(url) {
                headers {
                    append(HttpHeaders.UserAgent, USER_AGENTS.random())
                    append(HttpHeaders.AcceptLanguage, "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                }
            }
            if (!response.status.isSuccess()) {
                Log.w("HybridSearch", "Bing HTTP ${response.status.value}")
                return@withPermit emptyList()
            }

            val elements = Jsoup.parse(response.bodyAsText()).select("a.iusc[m]")
            if (elements.isEmpty()) Log.w("HybridSearch", "Bing sonuç döndürmedi (captcha / sayfa yapısı değişmiş olabilir)")

            elements.mapNotNull { el ->
                try {
                    val json = JSONObject(el.attr("m"))
                    val murl = json.optString("murl").takeIf { it.startsWith("http") }
                        ?: return@mapNotNull null
                    ImageCandidate(
                        imageUrl = murl,
                        thumbUrl = json.optString("turl").takeIf { it.startsWith("http") },
                        pageUrl = json.optString("purl").takeIf { it.startsWith("http") },
                        title = json.optString("t")
                    )
                } catch (e: Exception) {
                    null // Bozuk JSON: bu sonucu atla
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("HybridSearch", "Hata: ${e.message}")
            emptyList()
        }
    }

    /** En yüksek puanlı adayı ve puanını döndürür. */
    private fun pickBest(candidates: List<ImageCandidate>, queryTokens: List<String>): Pair<ImageCandidate, Int>? {
        return candidates
            .filterNot { isBadImage(it.imageUrl) }
            .mapIndexed { index, c -> c to score(c, queryTokens, index) }
            .maxByOrNull { it.second }
    }

    private fun score(c: ImageCandidate, queryTokens: List<String>, rank: Int): Int {
        var score = 0
        // Başlık ve sayfa adresi sorgu kelimeleriyle ne kadar örtüşüyor?
        val title = c.title.lowercase(trLocale)
        val haystack = title + " " + (c.pageUrl ?: "").lowercase(trLocale)
        val hits = queryTokens.count { token ->
            when {
                // Model numaraları tam kelime eşleşmeli: "Mi Band 8" ≠ "Mi Band 2"
                token.all { it.isDigit() } ->
                    Regex("(?<![\\p{L}\\p{N}])$token(?![\\p{L}\\p{N}])").containsMatchIn(haystack)
                token.length > 2 -> haystack.contains(token)
                else -> false
            }
        }
        score += hits * 2
        // Sorguda geçmeyen aksesuar kelimeleri: ürünün kendisi değil kılıfı / kordonu çıkmasın
        if (ACCESSORY_WORDS.any { it in title && it !in queryTokens }) score -= 4
        if (c.pageUrl != null && MarketPlaces.isMarketPlace(c.pageUrl)) score += 3
        if (MarketPlaces.isTrustedImageHost(c.imageUrl)) score += 1
        // Bing sıralamasına küçük bir ağırlık: ilk sonuçlar biraz önde
        score += (5 - rank).coerceAtLeast(0) / 2
        return score
    }

    // Market CDN'leri hotlink'e izin veriyor; diğer sitelerin resimleri sık sık 403 verdiği
    // için onlarda her zaman açılan Bing küçük resmini kullanıyoruz.
    private fun chooseDisplayImage(c: ImageCandidate): String =
        if (MarketPlaces.isTrustedImageHost(c.imageUrl) || c.thumbUrl == null) c.imageUrl else c.thumbUrl

    private fun tokenize(query: String): List<String> =
        query.lowercase(trLocale)
            .replace(Regex("[^\\p{L}\\p{N} ]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it !in STOP_WORDS }

    private fun isBadImage(url: String): Boolean {
        val lower = url.lowercase()
        val path = Uri.parse(lower).path ?: lower
        return lower.endsWith(".svg") ||
                lower.endsWith(".gif") ||
                lower.contains("110000644634147") ||
                BAD_IMAGE_REGEX.containsMatchIn(path)
    }
}
