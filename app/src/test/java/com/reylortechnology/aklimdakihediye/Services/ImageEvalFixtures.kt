package com.reylortechnology.aklimdakihediye.Services

import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ImageCandidate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.File
import java.util.Locale

/** Görsel seçimi değerlendirme seti: bir hediye ve seçilen fotoğrafın doğru sayılması için kurallar. */
@Serializable
data class ImageEvalCase(
    val id: String,
    @SerialName("gift_name") val giftName: String,
    @SerialName("search_query") val searchQuery: String,
    // Her grup "bunlardan biri geçmeli"; tüm gruplar sağlanmalı
    val must: List<List<String>>,
    // Hiçbiri geçmemeli. Sonu "$" ile biten kelime tam kelime olarak aranır ("pro$" ≠ "profesyonel")
    @SerialName("must_not") val mustNot: List<String>
)

enum class EvalMode(val suffix: String) {
    GiftName("name"),       // Bugünkü davranış: gift_name ile arama
    SearchQuery("query");   // Yeni davranış: Gemini'nin search_query alanı ile arama

    fun queryOf(case: ImageEvalCase) = if (this == GiftName) case.giftName else case.searchQuery
}

object ImageEvalFixtures {
    // Gradle birim testleri modül klasöründe (app/) çalışır
    private val root = File("src/test/resources/image_eval")
    val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    fun cases(): List<ImageEvalCase> =
        json.decodeFromString(File(root, "cases.json").readText())

    fun fixtureFile(case: ImageEvalCase, mode: EvalMode) =
        File(root, "bing/${case.id}_${mode.suffix}.json")

    fun loadCandidates(case: ImageEvalCase, mode: EvalMode): List<ImageCandidate>? =
        fixtureFile(case, mode).takeIf { it.exists() }?.let { json.decodeFromString(it.readText()) }

    /** Seçilen aday bu hediye için doğru mu? */
    fun isCorrect(case: ImageEvalCase, chosen: ImageCandidate?): Boolean {
        if (chosen == null) return false
        val hay = fold(chosen.title + " " + (chosen.pageUrl ?: ""))
        val mustOk = case.must.all { group -> group.any { hay.hasWord(fold(it), wholeWord = false) } }
        val mustNotOk = case.mustNot.none { raw ->
            val whole = raw.endsWith("$")
            hay.hasWord(fold(raw.removeSuffix("$")), whole)
        }
        return mustOk && mustNotOk
    }

    // Türkçe karakterleri ASCII'ye indirger, noktalama ve tireleri boşluk yapar:
    // "Kılıfı", "kilifi" ve URL'deki "kilifi-" aynı şekilde eşleşsin.
    fun fold(text: String): String =
        text.lowercase(Locale("tr"))
            .replace('ç', 'c').replace('ğ', 'g').replace('ı', 'i')
            .replace('ö', 'o').replace('ş', 's').replace('ü', 'u')
            .replace(Regex("[^a-z0-9]+"), " ")
            .trim()

    // Kelime başından eşleşir (Türkçe ekler serbest): "difuzor" → "difuzoru" da sayılır
    private fun String.hasWord(word: String, wholeWord: Boolean): Boolean {
        val end = if (wholeWord) "(?![a-z0-9])" else ""
        return Regex("(?<![a-z0-9])${Regex.escape(word)}$end").containsMatchIn(this)
    }
}
