package com.example.aklimdakihediye.Views.PartScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aklimdakihediye.Compose.SavedGiftCard
import com.example.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.ViewModels.SavedVariablesViewModel

@Composable
fun SavedGiftScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedVariablesViewModel = hiltViewModel(),
    navController: NavController,
    ) {

    BackHandler {
        navController.navigate(LocalNavController.MainScreen){
            popUpTo(0) {inclusive = true}
        }
    }

    val giftList = viewModel.savedGifts.collectAsState(initial = emptyList())
    Scaffold {
        when(giftList.value.isEmpty()){
            true -> EmptyGiftScreen(modifier = Modifier.padding(it),navController)
            false -> SuccesGiftScreen(modifier = Modifier.padding(it),giftList.value, navController =navController )
        }
    }
}


@Composable
fun EmptyGiftScreen(modifier: Modifier = Modifier,navController : NavController) {
    Column(
        modifier = modifier
            .paint(painter = painterResource(R.drawable.saved_screen_bg),
                contentScale = ContentScale.FillBounds
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.icon_back_2),
                "",
                modifier = Modifier
                    .clickable{
                        navController.navigate(LocalNavController.MainScreen){
                            popUpTo(0) {inclusive = true}
                        }
                    }
                    .size(50.dp)
            )

            Spacer(Modifier.weight(1f))
            Text(
                stringResource(R.string.saved_gift_screen_label),
                color =  Color.White,
                fontSize = 24.sp
            )
            Spacer(Modifier.weight(1f))
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(stringResource(
                R.string.saved_gift_screen_empty_label),
                fontSize = 30.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
@Composable
fun SuccesGiftScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    value: List<SavedGifts>,
    navController: NavController,
    viewModel : SavedVariablesViewModel = hiltViewModel()
){

    val deleteList = remember {
        mutableListOf<Int>()
    }
    Log.d("Tag empty List", "in fully list")
    val showDeleteButton = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .paint(
                painter = painterResource(R.drawable.saved_screen_bg),
                contentScale = ContentScale.FillBounds
            ),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.fillMaxHeight(0.05f))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.icon_back_2),
                "",
                modifier = Modifier
                    .clickable{
                        navController.navigate(LocalNavController.MainScreen){
                            popUpTo(0) {inclusive = true}
                        }
                    }
                    .size(50.dp)
            )

            Text(
                stringResource(R.string.saved_gift_screen_label),
                color =  Color.White,
                fontSize = 24.sp
            )

            Image(
                painter = painterResource(R.drawable.trash),
                "",
                modifier = Modifier
                    .clickable{
                        showDeleteButton.value = !showDeleteButton.value
                    }
                    .size(50.dp)
            )
        }
        Spacer(Modifier.fillMaxHeight(0.04f))
        LazyColumn (
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .weight(1f)
        ){
            items(
                value
            ) {
                val checked = remember { mutableStateOf(false) }
                if (checked.value){
                    deleteList.add(it.giftId)
                    Log.d("Tag added item" , it.giftId.toString())
                }else if(!checked.value){
                    deleteList.remove(it.giftId)
                    Log.d("Tag removed item" , it.giftId.toString())
                }
                SavedGiftCard(
                    gift = it,
                    checked = checked,
                    onCheckedChange = {checked.value = !checked.value},
                    enabled = showDeleteButton
                )

                Spacer(Modifier.height(10.dp))
            }
        }

        AnimatedVisibility(
            visible = showDeleteButton.value,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color.Red)
                    .clickable {
                        Log.d("Tag delete button", deleteList.toString())
                        viewModel.deleteGift(deleteList)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.delete), fontSize = 26.sp, color = Color.White)
            }
        }

    }
}