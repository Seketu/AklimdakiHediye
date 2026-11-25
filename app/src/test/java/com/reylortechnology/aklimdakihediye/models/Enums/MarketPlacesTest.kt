package com.reylortechnology.aklimdakihediye.models.Enums

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MarketPlacesTest {

    @Test
    fun addsParamWhenUrlHasNoQuery() {
        assertEquals(
            "https://www.amazon.com.tr/dp/B0X?tag=bizim-21",
            MarketPlaces.replaceQueryParam("https://www.amazon.com.tr/dp/B0X", "tag", "bizim-21")
        )
    }

    @Test
    fun replacesSomeoneElsesTagAndKeepsOtherParams() {
        assertEquals(
            "https://www.amazon.com.tr/dp/B0X?th=1&psc=1&tag=bizim-21#yorumlar",
            MarketPlaces.replaceQueryParam(
                "https://www.amazon.com.tr/dp/B0X?tag=baskasi-21&th=1&psc=1#yorumlar", "tag", "bizim-21"
            )
        )
    }

    @Test
    fun leavesNonMarketUrlsUntouched() {
        val url = "https://example.com/urun?tag=x"
        assertEquals(url, MarketPlaces.withAffiliate(url))
    }

    @Test
    fun searchUrlEncodesTurkishQuery() {
        val url = MarketPlaces.searchUrl("isme özel kupa")
        assertTrue(url, url.endsWith("isme+%C3%B6zel+kupa"))
    }
}
