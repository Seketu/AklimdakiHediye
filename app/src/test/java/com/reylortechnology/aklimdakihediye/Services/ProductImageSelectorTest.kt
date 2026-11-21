package com.reylortechnology.aklimdakihediye.Services

import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ImageCandidate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductImageSelectorTest {

    private val selector = ProductImageSelector()

    private fun candidate(title: String, page: String = "https://example.com/x", image: String = "https://example.com/a.jpg") =
        ImageCandidate(imageUrl = image, thumbUrl = "https://tse1.mm.bing.net/th?id=1", pageUrl = page, title = title)

    @Test
    fun parsesBingAsyncHtml() {
        val html = """
            <div><a class="iusc" m="{&quot;murl&quot;:&quot;https://cdn.dsmcdn.com/p/1.jpg&quot;,&quot;turl&quot;:&quot;https://tse1.mm.bing.net/th?id=OIP.1&quot;,&quot;purl&quot;:&quot;https://www.trendyol.com/x/okka-p-1&quot;,&quot;t&quot;:&quot;Arzum Okka Minio&quot;}"></a>
            <a class="iusc" m="bozuk json"></a>
            <a class="iusc" m="{&quot;murl&quot;:&quot;data:image/png;base64,xx&quot;}"></a></div>
        """.trimIndent()

        val result = selector.parseCandidates(html)

        assertEquals(1, result.size)
        assertEquals("https://cdn.dsmcdn.com/p/1.jpg", result[0].imageUrl)
        assertEquals("https://www.trendyol.com/x/okka-p-1", result[0].pageUrl)
        assertEquals("Arzum Okka Minio", result[0].title)
    }

    @Test
    fun tokenizeDropsPriceWordsButKeepsWordsContainingThem() {
        assertEquals(listOf("saatli", "outlet", "kupa"), selector.tokenize("Saatli outlet kupa fiyatı TL"))
    }

    @Test
    fun modelNumberMustMatchExactly() {
        val tokens = selector.tokenize("Xiaomi Mi Band 8")
        val wrong = candidate("Xiaomi Mi Band 2 Akıllı Bileklik")
        val right = candidate("Xiaomi Mi Band 8 Akıllı Bileklik")

        assertEquals(right, selector.selectBest(listOf(wrong, right), tokens))
    }

    @Test
    fun accessoryIsPenalised() {
        val tokens = selector.tokenize("Apple AirTag")
        val accessory = candidate("Apple AirTag Uyumlu Silikon Kılıf", page = "https://www.trendyol.com/a")
        val product = candidate("Apple AirTag Takip Cihazı")

        assertEquals(product, selector.selectBest(listOf(accessory, product), tokens))
    }

    @Test
    fun logoImagesAreSkipped() {
        val logo = candidate("Okka", image = "https://example.com/assets/logo.png")
        assertNull(selector.selectBest(listOf(logo), selector.tokenize("okka")))
    }

    @Test
    fun blanketImagesAreNotTreatedAsBlank() {
        val blanket = candidate("Elektrikli Battaniye", image = "https://example.com/blanket-blue.jpg")
        assertEquals(blanket, selector.selectBest(listOf(blanket), selector.tokenize("elektrikli battaniye")))
    }
}
