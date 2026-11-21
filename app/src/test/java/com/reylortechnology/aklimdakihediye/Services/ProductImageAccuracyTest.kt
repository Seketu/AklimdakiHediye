package com.reylortechnology.aklimdakihediye.Services

import org.junit.Assert.assertTrue
import org.junit.Assume.assumeTrue
import org.junit.Test
import java.io.File

/**
 * Kayıtlı Bing yanıtları üzerinde görsel seçiminin doğruluk oranını ölçer.
 * Rapor: app/build/reports/image_eval.txt
 */
class ProductImageAccuracyTest {

    private val selector = ProductImageSelector()

    // Algoritma değişikliği doğruluğu bu sınırın altına düşürürse test kırılır.
    // 2026-11-21 ölçümü: 29/30. Fixture'lar sabit olduğu için sonuç deterministik.
    private val MIN_SEARCH_QUERY_ACCURACY = 0.9

    @Test
    fun measureAccuracy() {
        val cases = ImageEvalFixtures.cases()
        assumeTrue(
            "Fixture yok, önce BingFixtureRecorder çalıştırılmalı",
            cases.all { case -> EvalMode.entries.all { ImageEvalFixtures.fixtureFile(case, it).exists() } }
        )

        val report = StringBuilder()
        val correct = mutableMapOf<String, Int>()

        for (case in cases) {
            report.appendLine("## ${case.id}")
            for (mode in EvalMode.entries) {
                val candidates = ImageEvalFixtures.loadCandidates(case, mode).orEmpty()
                val tokens = selector.tokenize(mode.queryOf(case))

                // Karşılaştırma için: Bing'in ilk sonucunu körü körüne almak
                val bingFirst = candidates.firstOrNull()
                val chosen = selector.selectBest(candidates, tokens)

                val firstOk = ImageEvalFixtures.isCorrect(case, bingFirst)
                val chosenOk = ImageEvalFixtures.isCorrect(case, chosen)
                correct.merge("${mode.suffix}/bing-first", if (firstOk) 1 else 0, Int::plus)
                correct.merge("${mode.suffix}/selector", if (chosenOk) 1 else 0, Int::plus)

                report.appendLine(
                    "  [${mode.suffix}] ${if (chosenOk) "✓" else "✗"} ${chosen?.title?.take(80) ?: "—"}" +
                            "   (bing-first ${if (firstOk) "✓" else "✗"})"
                )
            }
        }

        val total = cases.size
        val summary = buildString {
            appendLine("=== Doğruluk ($total hediye) ===")
            for (mode in EvalMode.entries) {
                for (algo in listOf("bing-first", "selector")) {
                    val n = correct["${mode.suffix}/$algo"] ?: 0
                    appendLine("  ${mode.suffix.padEnd(5)} / ${algo.padEnd(10)}: $n/$total")
                }
            }
        }

        val text = summary + "\n" + report
        println(text)
        File("build/reports").mkdirs()
        File("build/reports/image_eval.txt").writeText(text)

        val accuracy = (correct["${EvalMode.SearchQuery.suffix}/selector"] ?: 0).toDouble() / total
        assertTrue("search_query doğruluğu $accuracy < $MIN_SEARCH_QUERY_ACCURACY", accuracy >= MIN_SEARCH_QUERY_ACCURACY)
    }
}
