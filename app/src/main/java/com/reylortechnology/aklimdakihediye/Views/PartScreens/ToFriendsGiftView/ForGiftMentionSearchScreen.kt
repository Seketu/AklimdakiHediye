package com.reylortechnology.aklimdakihediye.Views.PartScreens.ToFriendsGiftView

import android.content.Context

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.Views.PartScreens.LoadingScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.SuccessSearchResultScreen
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products

@Composable
fun ForGiftMentionSearchScreen(
    modifier: Modifier = Modifier,
    geminiQueryState : QueryProductsStatus,
    searchResultStatus: SearchResultStatus,
    navController: NavController,
    takeSearchResultStatus: (List<Products>) -> Unit,
    saveGifts : (SavedGifts)-> Unit,
    context : Context
) {
    when (geminiQueryState) {

        is QueryProductsStatus.Loading -> {
            LoadingScreen(modifier = modifier)
        }

        is QueryProductsStatus.Error -> Text(
            text = geminiQueryState.message,
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        is QueryProductsStatus.Success -> SuccessSearchResultScreen(
            modifier = modifier,
            navController = navController,
            searchResultState = searchResultStatus,
            getSearchResult = {
                takeSearchResultStatus(geminiQueryState.products)
            },
            saveGift = {
                saveGifts(
                    SavedGifts(
                        giftName = it.name,
                        giftUrl = it.url,
                        giftImage = it.imageUrl ?: "",
                    )
                )
            },
            context = context
        )

        QueryProductsStatus.None -> {}
    }
}

@Preview
@Composable
private fun ForGiftMentionSearchPreview() {
    ForGiftMentionSearchScreen(
        modifier = Modifier.fillMaxSize(),
        geminiQueryState = QueryProductsStatus.Loading,
        searchResultStatus = SearchResultStatus.None,
        navController = rememberNavController(),
        takeSearchResultStatus = {},
        saveGifts = {},
        context = LocalContext.current
    )
}