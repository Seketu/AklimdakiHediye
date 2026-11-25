package com.reylortechnology.aklimdakihediye.models.Enums

import com.reylortechnology.aklimdakihediye.BuildConfig
import java.net.URLEncoder

enum class MarketPlaces(
    val domain : String,
    val imageHost : String?,                  // Hotlink engeli olmayan resim CDN'i
    val searchUrlPrefix : String? = null,     // Ürün bulunamazsa açılacak arama sayfası; sorgu sona eklenir
    val affiliateParam : String? = null,      // Affiliate kimliğinin taşındığı URL parametresi
    val affiliateId : String = ""             // local.properties'ten gelir; boşsa affiliate kapalı
) {
    Trendyol(domain = "trendyol.com", imageHost = "dsmcdn.com", searchUrlPrefix = "https://www.trendyol.com/sr?q="),
    Hepsiburada(domain = "hepsiburada.com", imageHost = "productimages.hepsiburada.net"),
    Amazon(
        domain = "amazon.com.tr",
        imageHost = "media-amazon.com",
        searchUrlPrefix = "https://www.amazon.com.tr/s?k=",
        affiliateParam = "tag",
        affiliateId = BuildConfig.AMAZON_AFFILIATE_TAG
    ),
    Akakce(domain = "akakce.com", imageHost = "cdn.akakce.com"),
    CicekSepeti(domain = "ciceksepeti.com", imageHost = "ciceksepeti.com"),
    Morhipo(domain = "morhipo.com", imageHost = null),
    Boyner(domain = "boyner.com.tr", imageHost = null);

    val hasAffiliate : Boolean
        get() = affiliateParam != null && affiliateId.isNotBlank()

    companion object {
        fun isMarketPlace(url : String) : Boolean {
            val host = hostOf(url) ?: return false
            return entries.any { host.matchesDomain(it.domain) }
        }

        fun isTrustedImageHost(url : String) : Boolean {
            val host = hostOf(url) ?: return false
            return entries.any { market -> market.imageHost?.let { host.matchesDomain(it) } == true }
        }

        /**
         * Link affiliate programı olan bir markete aitse kimliğimizi ekler. Linkte başka birinin
         * kimliği varsa (Bing sonuçlarında sık olur) onu bizimkiyle değiştirir.
         * Kayıtlı hediyeler de güncel kimlikle açılsın diye tıklama anında çağrılır.
         */
        fun withAffiliate(url : String) : String {
            val host = hostOf(url) ?: return url
            val market = entries.firstOrNull { host.matchesDomain(it.domain) } ?: return url
            if (!market.hasAffiliate) return url
            return replaceQueryParam(url, market.affiliateParam!!, market.affiliateId)
        }

        /** Ürün sayfası bulunamadığında açılacak arama linki: affiliate'i aktif market öncelikli. */
        fun searchUrl(query : String) : String {
            val market = entries.firstOrNull { it.hasAffiliate && it.searchUrlPrefix != null } ?: Trendyol
            return market.searchUrlPrefix + URLEncoder.encode(query, "UTF-8")
        }

        internal fun replaceQueryParam(url : String, name : String, value : String) : String {
            val fragment = url.substringAfter('#', "")
            val withoutFragment = url.substringBefore('#')
            val path = withoutFragment.substringBefore('?')
            val params = withoutFragment.substringAfter('?', "")
                .split('&')
                .filter { it.isNotEmpty() && it.substringBefore('=') != name }
            val query = (params + "$name=${URLEncoder.encode(value, "UTF-8")}").joinToString("&")
            return "$path?$query" + if (fragment.isNotEmpty()) "#$fragment" else ""
        }

        // android.net.Uri yerine düz metin: birim testlerinde de çalışsın, Türkçe karakterli
        // ya da boşluklu adreslerde java.net.URI gibi hata fırlatmasın.
        private fun hostOf(url : String) : String? =
            Regex("^[a-zA-Z][a-zA-Z0-9+.-]*://(?:[^@/?#]*@)?([^/:?#]+)").find(url)
                ?.groupValues?.get(1)?.lowercase()

        private fun String.matchesDomain(domain : String) : Boolean =
            this == domain || endsWith(".$domain")
    }
}
