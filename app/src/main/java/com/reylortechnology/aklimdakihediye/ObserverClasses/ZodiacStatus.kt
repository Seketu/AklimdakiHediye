package com.reylortechnology.aklimdakihediye.ObserverClasses

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class ZodiacStatus(
    @param:DrawableRes val iconRes: Int,
    @param:StringRes val nameRes: Int
) {
    Libra(R.drawable.libra, R.string.zodiac_name_libra),
    Virgo(R.drawable.virgo, R.string.zodiac_name_virgo),
    Scorpio(R.drawable.scorpio, R.string.zodiac_name_scorpio),
    Sagittarius(R.drawable.sagittarius, R.string.zodiac_name_sagittarius),
    Capricorn(R.drawable.capricorn, R.string.zodiac_name_capricon),
    Aquarius(R.drawable.aquarius_, R.string.zodiac_name_aquarius),
    Pisces(R.drawable.pisces, R.string.zodiac_name_pisces),
    Aries(R.drawable.aries, R.string.zodiac_name_aries),
    Taurus(R.drawable.taurus, R.string.zodiac_name_taurus),
    Gemini(R.drawable.gemini, R.string.zodiac_name_gemini),
    Cancer(R.drawable.cancer, R.string.zodiac_name_cancer),
    Leo(R.drawable.leo, R.string.zodiac_name_leo),
    None(0, 0)
}