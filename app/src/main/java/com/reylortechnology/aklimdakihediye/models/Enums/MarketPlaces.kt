package com.reylortechnology.aklimdakihediye.models.Enums

enum class MarketPlaces(
    val domain : String,
    val imageHost : String?   // Hotlink engeli olmayan resim CDN'i
) {
    Trendyol(domain = "trendyol.com", imageHost = "dsmcdn.com"),
    Hepsiburada(domain = "hepsiburada.com", imageHost = "productimages.hepsiburada.net"),
    Amazon(domain = "amazon.com.tr", imageHost = "media-amazon.com"),
    Akakce(domain = "akakce.com", imageHost = "cdn.akakce.com"),
    CicekSepeti(domain = "ciceksepeti.com", imageHost = "ciceksepeti.com"),
    Morhipo(domain = "morhipo.com", imageHost = null),
    Boyner(domain = "boyner.com.tr", imageHost = null);

    companion object {
        fun isMarketPlace(url : String) : Boolean {
            val host = hostOf(url) ?: return false
            return entries.any { host.matchesDomain(it.domain) }
        }

        fun isTrustedImageHost(url : String) : Boolean {
            val host = hostOf(url) ?: return false
            return entries.any { market -> market.imageHost?.let { host.matchesDomain(it) } == true }
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
