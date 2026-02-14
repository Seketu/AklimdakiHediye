package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class Hobbies(
    val id : Int,
    @param:StringRes val textSource : Int,
    @param:DrawableRes val iconSource : Int? = null
)
{
    Sport(id = 0 , textSource = R.string.hobbies_spor,R.drawable.spor_icon),
    Gym(id = 1 , textSource = R.string.hobbies_gym,R.drawable.gym_icon),
    Fashion(id = 2 , textSource = R.string.hobbies_fashion,R.drawable.fashion_icon),
    Ghosts(id = 3 , textSource = R.string.hobbies_ghosts,R.drawable.ghosts_icon),
    Music(id = 4 , textSource = R.string.hobbies_music,R.drawable.music_icon),
    Instruments(id = 5 , textSource = R.string.hobbies_enstrumants,R.drawable.enstrument_icon),
    Movies(id = 6 , textSource = R.string.hobbies_movies,R.drawable.movies_icon),
    Series(id = 7 , textSource = R.string.hobbies_series,R.drawable.series_icon),
    Cook(id = 8 , textSource = R.string.hobbies_cook,R.drawable.cooking_icon),
    Shopping(id = 9 , textSource = R.string.hobbies_shopping,R.drawable.shopping_icon),
    Reading(id = 10 , textSource = R.string.hobbies_reading,R.drawable.reading_icon),
    VideoGames(id = 11 , textSource = R.string.hobbies_video_games,R.drawable.video_games_icon),
    Chest(id = 12 , textSource = R.string.hobbies_chest,R.drawable.chest_icon),
    Zodiacs(id = 13 , textSource = R.string.hobbies_zodiacs,R.drawable.zodiacs_icon),
    Camping(id = 14 , textSource = R.string.hobbies_camping,R.drawable.camp_icon),
    Walking(id = 15 , textSource = R.string.hobbies_walking,R.drawable.walking_icon),
    Flowers(id = 16 , textSource = R.string.hobbies_flowers,R.drawable.flowers_icon),
    Swimming(id = 17 , textSource = R.string.hobbies_swimming, R.drawable.swim_icon),
    Dance(id = 18 , textSource = R.string.hobbies_dance , R.drawable.dance_icon),
    Bike(id = 19 , textSource = R.string.hobbies_bike , R.drawable.bike_icon),
    MotoBike(id = 20 , textSource = R.string.hobbies_motobike, R.drawable.motocycle_icon),
    Fishing(id = 21 , textSource = R.string.hobbies_fishing , R.drawable.fishing_icon),
    Cars(id = 22 , textSource = R.string.hobbies_cars,R.drawable.cars_icon),
    Football(id = 23 , textSource = R.string.hobbies_football , R.drawable.football_icon),
    Basketball(id = 24 , textSource = R.string.hobbies_basketball , R.drawable.basketball_icon),
    Volleyball(id = 25 , textSource = R.string.hobbies_volleyball , R.drawable.volleyball_icon),
    Cosplay(id = 26 , textSource = R.string.hobbies_cosplay, R.drawable.cosplay_icon),
    Art(id = 27 , textSource = R.string.hobbies_art , R.drawable.art_icon)
}