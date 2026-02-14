package com.reylortechnology.aklimdakihediye.Services

import android.net.Uri
import android.util.Log
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ProductResult
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class ImageSearchService @Inject constructor(
    private val client: HttpClient
) {
    private val USER_AGENTS = listOf(
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36"
    )

    // İzin verdiğimiz alışveriş siteleri (Whitelist)
    private val MARKET_DOMAINS = listOf(
        "trendyol.com",
        "hepsiburada.com",
        "amazon.com.tr",
        "akakce.com",
        "ciceksepeti.com",
        "morhipo.com",
        "boyner.com.tr"
    )

    suspend fun searchProduct(query: String): ProductResult = withContext(Dispatchers.IO) {
        try {
            delay(Random.nextLong(200, 600))

            // 1. Sorgu Temizliği
            val cleanQuery = query.lowercase(Locale("tr"))
                .replace(Regex("[^a-z0-9ğüşıöç ]"), "")
                .replace("fiyat", "")
                .replace("tl", "")
                .trim()

            Log.d("HybridSearch", "🔍 Aranıyor: $cleanQuery")

            // 2. ARAMA URL'İ (GLOBAL ARAMA)
            // Site filtresi YOK. Resim her yerden gelebilir.
            val url = "https://www.bing.com/images/search?q=${Uri.encode(cleanQuery)}&first=1&count=40&qft=+filterui:imagesize-medium"

            val response = client.get(url) {
                headers {
                    append(HttpHeaders.UserAgent, USER_AGENTS.random())
                    append(HttpHeaders.AcceptLanguage, "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                }
            }

            val html = response.bodyAsText()
            val doc = Jsoup.parse(html)

            // --- AYRIŞTIRMA MANTIĞI ---
            var finalImage: String? = null
            var finalProductLink: String? = null

            val elements = doc.select(".iusc")

            for (element in elements) {
                val mData = element.attr("m")
                if (mData.isEmpty()) continue

                val murl = extractJsonValue(mData, "murl") // Resim Linki
                val purl = extractJsonValue(mData, "purl") // Ürün Linki

                // 1. RESİM SEÇİMİ (Henüz bulmadıysak)
                if (finalImage == null && murl != null && !isBadImage(murl)) {
                    finalImage = cleanUrl(murl)
                    Log.d("HybridSearch", "🖼️ Resim bulundu (Global): $finalImage")
                }

                // 2. LİNK SEÇİMİ (Henüz bulmadıysak)
                // Linkin "purl" değeri bizim market listemizden biri mi?
                if (finalProductLink == null && purl != null) {
                    val isMarketPlace = MARKET_DOMAINS.any { domain -> purl.contains(domain) }

                    if (isMarketPlace) {
                        finalProductLink = cleanUrl(purl)
                        Log.d("HybridSearch", "🔗 Market Linki bulundu: $finalProductLink")
                    }
                }

                // İkisini de bulduysak döngüden erken çık, vakit kaybetme
                if (finalImage != null && finalProductLink != null) break
            }

            // --- FALLBACK (YEDEK PLAN) ---
            // Eğer Bing sonuçlarında Trendyol/Hepsiburada linki çıkmadıysa,
            // Google arama linki YERİNE, direkt Trendyol Arama linki oluşturuyoruz.
            if (finalProductLink == null) {
                finalProductLink = "https://www.trendyol.com/sr?q=${Uri.encode(cleanQuery)}"
                Log.w("HybridSearch", "⚠️ Market linki bulunamadı, Trendyol araması oluşturuldu.")
            }

            // HTML Fallback Resim (JSON başarısız olursa)
            if (finalImage == null) {
                val img = doc.select("img[src^=http]").firstOrNull {
                    val src = it.attr("src")
                    src.length > 80 && !src.contains("bing.net/th?id=")
                }
                if (img != null) finalImage = cleanUrl(img.attr("src"))
            }

            if (finalImage != null) {
                return@withContext ProductResult(finalProductLink, finalImage)
            } else {
                Log.e("HybridSearch", "❌ Hiçbir görsel bulunamadı.")
                return@withContext ProductResult(finalProductLink, null)
            }

        } catch (e: Exception) {
            Log.e("HybridSearch", "Hata: ${e.message}")
            ProductResult(null, null)
        }
    }

    private fun extractJsonValue(json: String, key: String): String? {
        val regex = "\"$key\":\"([^\"]+)\"".toRegex()
        return regex.find(json)?.groupValues?.get(1)
    }

    private fun cleanUrl(url: String): String {
        return try {
            var temp = url.replace("\\u002f", "/")
                .replace("\\u0026", "&")
                .replace("\\u003d", "=")
                .replace("\\u0025", "%")
                .replace("\\/", "/")
            if (temp.contains("%")) temp = Uri.decode(temp)
            temp
        } catch (e: Exception) { url }
    }

    private fun isBadImage(url: String): Boolean {
        val lowerUrl = url.lowercase()
        return lowerUrl.contains("logo") ||
                lowerUrl.contains("icon") ||
                lowerUrl.contains("placeholder") ||
                lowerUrl.contains("blank") ||
                lowerUrl.endsWith(".svg") ||
                lowerUrl.contains("110000644634147")
    }
}