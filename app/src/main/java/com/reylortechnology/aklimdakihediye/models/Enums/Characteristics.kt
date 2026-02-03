package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class CharacterTrait(
    val id: Int,
    @param:StringRes val textSource: Int
) {
    // Kişisel Güç
    DETERMINATION(id = 1, R.string.trait_determination),
    SELF_DISCIPLINE(id = 2, R.string.trait_self_discipline),
    RESILIENCE(id = 3, R.string.trait_resilience),
    INTEGRITY(id = 4, R.string.trait_integrity),

    // Sosyal ve Duygusal
    EMPATHY(id = 5, R.string.trait_empathy),
    SINCERITY(id = 6, R.string.trait_sincerity),
    ADAPTABILITY(id = 7, R.string.trait_adaptability),
    HUMILITY(id = 8, R.string.trait_humility),

    // Zihinsel
    CURIOSITY(id = 9, R.string.trait_curiosity),
    ANALYTICAL_THINKING(id = 10, R.string.trait_analytical_thinking),
    CREATIVITY(id = 11, R.string.trait_creativity),
    OPEN_MINDEDNESS(id = 12, R.string.trait_open_mindedness),

    // Davranışsal
    PERFECTIONISM(id = 13, R.string.trait_perfectionism),
    METICULOUSNESS(id = 14, R.string.trait_meticulousness),
    COMPOSURE(id = 15, R.string.trait_composure),
    STUBBORNNESS(id = 16, R.string.trait_stubbornness)
}