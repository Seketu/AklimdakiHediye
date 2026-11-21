package com.reylortechnology.aklimdakihediye.Services

import org.junit.Assume.assumeTrue
import org.junit.Test
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * Değerlendirme setinin Bing yanıtlarını canlı olarak çekip fixture olarak kaydeder.
 * Normal test koşusunda atlanır; sadece fixture'ları yenilemek için:
 *
 *   $env:RECORD_IMAGE_FIXTURES='1'; .\gradlew.bat :app:testDebugUnitTest --tests "*BingFixtureRecorder*"
 */
class BingFixtureRecorder {

    private val selector = ProductImageSelector()

    @Test
    fun recordFixtures() {
        assumeTrue(System.getenv("RECORD_IMAGE_FIXTURES") == "1")

        for (case in ImageEvalFixtures.cases()) {
            for (mode in EvalMode.entries) {
                // Uygulamadaki ImageSearchService ile aynı sorgu temizliği
                val query = selector.tokenize(mode.queryOf(case)).joinToString(" ")
                val candidates = selector.parseCandidates(fetchBing(query))
                val file = ImageEvalFixtures.fixtureFile(case, mode)
                file.parentFile?.mkdirs()
                file.writeText(ImageEvalFixtures.json.encodeToString(candidates))
                println("${case.id} [${mode.suffix}] → ${candidates.size} aday")
                Thread.sleep(1200)
            }
        }
    }

    private fun fetchBing(query: String): String {
        val url = "https://www.bing.com/images/async?q=${URLEncoder.encode(query, "UTF-8")}" +
                "&first=0&count=35&mmasync=1&mkt=tr-TR&qft=+filterui:imagesize-medium"
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.setRequestProperty(
            "User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
        )
        connection.setRequestProperty("Accept-Language", "tr-TR,tr;q=0.9,en-US;q=0.8,en;q=0.7")
        return connection.inputStream.use { it.readBytes().toString(Charsets.UTF_8) }
    }
}
