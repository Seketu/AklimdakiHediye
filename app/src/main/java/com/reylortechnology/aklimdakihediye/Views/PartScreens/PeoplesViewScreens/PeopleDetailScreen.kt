package com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.R

@Composable
fun PeopleDetailScreen(
    modifier: Modifier = Modifier,peoples: Peoples
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ){

    }
}

@Preview
@Composable
private fun PeopleDetailPrev() {
    PeopleDetailScreen(peoples = Peoples(
        peopleId = 0,
        peopleName = "Reyhan",
        relationship = com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship.Friends,
        birthday = java.time.LocalDate.now(),
        age = 25,
        zodiac = "Koç",
        hobbies = "Kitap okumak, seyahat etmek",
        bestSide = "Dost canlısı ve yardımsever",
        character = "Neşeli ve pozitif",
        job = "Yazılım Mühendisi",
        image = R.drawable.woman_2,
        color = androidx.compose.ui.graphics.Color(0xFFFFC107)
    )
    )
}