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
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import kotlin.random.Random

class ImageSearchService @Inject constructor(
    private val client: HttpClient,
    private val selector: ProductImageSelector
) {
    // Hilt'te @Singleton sağlanıyor; bu yüzden ikisi de tüm uygulama için tek.
    // Aynı anda en fazla 3 istek: 10 paralel istek Bing'de captcha'ya düşürüyordu.
    private val requestLimiter = Semaphore(3)
    private val cache = ConcurrentHashMap<String, ProductResult>()

    private val USER_AGENTS = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
    )

    suspend fun searchProduct(query: String): ProductResult = withContext(Dispatchers.IO) {
        val tokens = selector.tokenize(query)
        val cleanQuery = tokens.joinToString(" ")
        if (cleanQuery.isBlank()) return@withContext ProductResult(null, null)

        cache[cleanQuery]?.let { return@withContext it }

        Log.d("HybridSearch", "🔍 Aranıyor: $cleanQuery")

        // Market sonuçları puanlamada öne çıkarılıyor; Bing görsellerinde "site:" filtresi
        // neredeyse hiç market sonucu döndürmediği için kullanılmıyor.
        val candidate = selector.selectBest(fetchCandidates(cleanQuery), tokens)

        val productLink = candidate?.pageUrl?.takeIf { MarketPlaces.isMarketPlace(it) }
            ?: "https://www.trendyol.com/sr?q=${Uri.encode(cleanQuery)}"

        val image = candidate?.let { selector.chooseDisplayImage(it) }
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

            val candidates = selector.parseCandidates(response.bodyAsText())
            if (candidates.isEmpty()) Log.w("HybridSearch", "Bing sonuç döndürmedi (captcha / sayfa yapısı değişmiş olabilir)")
            candidates
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("HybridSearch", "Hata: ${e.message}")
            emptyList()
        }
    }
}
