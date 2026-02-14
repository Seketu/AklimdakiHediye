package com.reylortechnology.aklimdakihediye.Services

import android.net.Uri
import android.util.Log
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ProductResult
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton // Servisin tek bir örneği olmalı ki Mutex işe yarasın
class DuckDuckGoSearch @Inject constructor(
    private val client: HttpClient
) {
    // Bu kilit, aynı anda sadece 1 aramanın yapılmasını garanti eder.
    private val searchMutex = Mutex()

    // Rastgele User-Agent havuzu (Sürekli aynı kimlikle gitmemek için)
    private val userAgents = listOf(
        "Mozilla/5.0 (Linux; Android 13; SM-S908B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 12; Pixel 6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Mobile Safari/537.36",
        "Mozilla/5.0 (Linux; Android 11; Samsung SM-A525F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Mobile Safari/537.36"
    )

    suspend fun searchProduct(query: String): ProductResult = withContext(Dispatchers.IO) {
        // --- ADIM 1: KUYRUK VE BEKLEME (EN ÖNEMLİ KISIM) ---
        // Diğer aramalar bitene kadar burada bekler. Asla aynı anda 2 istek atmaz.
        searchMutex.withLock {

            // "İnsan" taklidi: Arama yapmadan önce düşünme payı
            val thinkingTime = Random.nextLong(2000, 5000) // 2 ile 5 saniye arası bekle
            Log.d("HumanDuck", "Sıra bende. İnsan gibi davranılıyor... Bekleme: ${thinkingTime}ms")
            delay(thinkingTime)

            try {
                val currentAgent = userAgents.random() // Her aramada farklı cihaz gibi görün

                // --- ADIM 2: LITE VERSİYON (POST İSTEĞİ) ---
                // Lite versiyonu form submit (POST) ile çağırmak daha güvenilirdir.
                val siteFilter = "site:trendyol.com OR site:hepsiburada.com OR site:amazon.com.tr"
                val finalQuery = "$query $siteFilter"

                Log.d("HumanDuck", "Aranıyor: $finalQuery")

                val response = client.submitForm(
                    url = "https://lite.duckduckgo.com/lite/",
                    formParameters = parameters {
                        append("q", finalQuery)
                        append("kl", "tr-tr") // Türkiye Lokasyonu
                        append("dt", "w") // Zaman: Tüm zamanlar
                    }
                ) {
                    headers {
                        append(HttpHeaders.UserAgent, currentAgent)
                        append(HttpHeaders.Referrer, "https://lite.duckduckgo.com/")
                        append(HttpHeaders.Origin, "https://lite.duckduckgo.com")
                        append(HttpHeaders.ContentType, "application/x-www-form-urlencoded")
                        // Gerçekçi Chrome Headerları
                        append("Sec-Ch-Ua", "\"Chromium\";v=\"124\", \"Google Chrome\";v=\"124\", \"Not-A.Brand\";v=\"99\"")
                        append("Sec-Ch-Ua-Mobile", "?1")
                        append("Sec-Ch-Ua-Platform", "\"Android\"")
                        append("Sec-Fetch-Dest", "document")
                        append("Sec-Fetch-Mode", "navigate")
                        append("Sec-Fetch-Site", "same-origin")
                        append(HttpHeaders.AcceptLanguage, "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
                    }
                }

                val html = response.bodyAsText()

                // --- ADIM 3: ENGEL KONTROLÜ ---
                if (html.length < 2000 || html.contains("anomaly-modal")) {
                    Log.e("HumanDuck", "Hâlâ şüpheli görünüyoruz! Captcha'ya takıldık.")
                    // Eğer engellendiysek bir sonrakini daha uzun bekletmek için buraya ekstra delay koyabiliriz
                    delay(5000)
                    return@withLock ProductResult(null, null)
                }

                // --- ADIM 4: PARSE İŞLEMİ ---
                val doc = Jsoup.parse(html)

                // Lite versiyonda sonuçlar tablo içindedir. Class isimleri bazen değişir.
                // En garantisi ".result-link" sınıfıdır.
                val resultLink = doc.selectFirst(".result-link")?.attr("href")

                if (resultLink.isNullOrEmpty()) {
                    Log.w("HumanDuck", "Sonuç bulunamadı.")
                    return@withLock ProductResult(null, null)
                }

                // Link temizleme (Bazen // ile başlar)
                val cleanUrl = if (resultLink.startsWith("//")) "https:$resultLink" else resultLink
                val decodedUrl = decodeDuckLink(cleanUrl)

                Log.d("HumanDuck", "Link Bulundu: $decodedUrl")

                // --- ADIM 5: RESİM ÇEKME (JSON-LD Yöntemi) ---
                // Resim için siteye gitmek risklidir ama linki bulduysak deneyelim.
                // Buradaki User-Agent ile Arama User-Agent'ı aynı olmalı!
                val image = fetchImageHumanly(decodedUrl, currentAgent)

                return@withLock ProductResult(decodedUrl, image)

            } catch (e: Exception) {
                Log.e("HumanDuck", "Hata: ${e.message}")
                return@withLock ProductResult(null, null)
            }
        }
    }

    private fun decodeDuckLink(link: String): String {
        return try {
            val uri = Uri.parse(link)
            val encoded = uri.getQueryParameter("uddg")
            if (encoded != null) Uri.decode(encoded) else link
        } catch (e: Exception) { link }
    }

    private suspend fun fetchImageHumanly(url: String, userAgent: String): String? {
        if (!url.startsWith("http")) return null

        // Trendyol/Hepsiburada'ya istek atarken arama motorundan gelmişiz gibi yapalım
        return try {
            val response = client.get(url) {
                headers {
                    append(HttpHeaders.UserAgent, userAgent)
                    append(HttpHeaders.Referrer, "https://www.google.com/") // Google'dan gelmiş gibi yap
                    append(HttpHeaders.AcceptLanguage, "tr-TR,tr;q=0.9")
                }
            }

            if (response.status != HttpStatusCode.OK) return null

            val doc = Jsoup.parse(response.bodyAsText())

            // Öncelik JSON-LD (Script içindeki gizli veri)
            var img = ""
            val scripts = doc.select("script[type=application/ld+json]")
            for (script in scripts) {
                val data = script.data()
                if (data.contains("\"image\"")) {
                    // Basit regex ile image url yakala
                    val pattern = "\"image\"\\s*:\\s*[\"\\[]?\\s*\"(https?://[^\"]+)\"".toRegex()
                    val match = pattern.find(data)
                    if (match != null) {
                        img = match.groupValues[1].replace("\\/", "/")
                        break
                    }
                }
            }

            // Yoksa Open Graph
            if (img.isEmpty()) img = doc.select("meta[property=og:image]").attr("content")

            if (img.isNotEmpty()) img else null
        } catch (e: Exception) {
            null
        }
    }
}