package com.reylortechnology.aklimdakihediye.Services

import com.reylortechnology.aklimdakihediye.models.Enums.MarketPlaces
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ImageCandidate
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.jsoup.Jsoup
import java.util.Locale

/**
 * Bing görsel sonuçlarından hediyeye en uygun fotoğrafı seçen saf mantık.
 * Android bağımlılığı yok; bu sayede birim testlerinde doğruluk oranı ölçülebiliyor.
 */
class ProductImageSelector {

    // Sorgudan atılacak kelimeler (alt string değil, tam kelime olarak)
    private val STOP_WORDS = setOf("fiyat", "fiyatı", "fiyatları", "tl", "₺", "ve", "ile", "için", "bir")

    private val ACCESSORY_WORDS = setOf("uyumlu", "kılıf", "kordon", "kayış", "yedek", "aparat", "koruyucu")

    // Sadece yol parçası olarak geçerse kötü say ("blanket", "silicone" gibi kelimeleri elemesin)
    private val BAD_IMAGE_REGEX =
        Regex("(^|[/_\\-.])(logo|icon|favicon|placeholder|sprite|banner|avatar|noimage|no-image)([/_\\-.]|$)")

    private val trLocale = Locale("tr")

    fun tokenize(query: String): List<String> =
        query.lowercase(trLocale)
            .replace(Regex("[^\\p{L}\\p{N} ]"), " ")
            .split(Regex("\\s+"))
            .filter { it.isNotBlank() && it !in STOP_WORDS }

    /** Bing "/images/async" HTML'indeki her sonucun "m" JSON'unu adaya çevirir. */
    fun parseCandidates(html: String): List<ImageCandidate> =
        Jsoup.parse(html).select("a.iusc[m]").mapNotNull { el ->
            try {
                val json = Json.parseToJsonElement(el.attr("m")).jsonObject
                val murl = json.httpValue("murl") ?: return@mapNotNull null
                ImageCandidate(
                    imageUrl = murl,
                    thumbUrl = json.httpValue("turl"),
                    pageUrl = json.httpValue("purl"),
                    title = json["t"]?.jsonPrimitive?.contentOrNull.orEmpty()
                )
            } catch (e: Exception) {
                null // Bozuk JSON: bu sonucu atla
            }
        }

    /** En yüksek puanlı adayı döndürür. */
    fun selectBest(candidates: List<ImageCandidate>, queryTokens: List<String>): ImageCandidate? =
        candidates
            .filterNot { isBadImage(it.imageUrl) }
            .mapIndexed { index, c -> c to score(c, queryTokens, index) }
            .maxByOrNull { it.second }
            ?.first

    // Market CDN'leri hotlink'e izin veriyor; diğer sitelerin resimleri sık sık 403 verdiği
    // için onlarda her zaman açılan Bing küçük resmini kullanıyoruz.
    fun chooseDisplayImage(c: ImageCandidate): String =
        if (MarketPlaces.isTrustedImageHost(c.imageUrl) || c.thumbUrl == null) c.imageUrl else c.thumbUrl

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

    private fun isBadImage(url: String): Boolean {
        val lower = url.lowercase()
        val path = lower.substringAfter("://").substringAfter('/', "").substringBefore('?').substringBefore('#')
        return lower.endsWith(".svg") ||
                lower.endsWith(".gif") ||
                lower.contains("110000644634147") ||
                BAD_IMAGE_REGEX.containsMatchIn("/$path")
    }

    private fun JsonObject.httpValue(key: String): String? =
        this[key]?.jsonPrimitive?.contentOrNull?.takeIf { it.startsWith("http") }
}
