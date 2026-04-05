package com.reylortechnology.aklimdakihediye.Views.PartScreens

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.Compose.SearchCard
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.CelebretadDayViewModel
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel


//Todo update all search screen
@Composable
fun SearchResultScreenCelebratedDays(
    viewModel: CelebretadDayViewModel = hiltViewModel(),
    navController: NavController
) {
    val queryState = viewModel.queryState.collectAsState()

    val searchResultState = viewModel.totalState.collectAsState()

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {paddingValues ->
        queryState.value.let {
            when (it) {
                is QueryProductsStatus.Error -> Text(
                    modifier = Modifier.padding(paddingValues),
                    text = it.message,
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                is QueryProductsStatus.Loading -> LoadingScreen(modifier = Modifier.padding(paddingValues))
                is QueryProductsStatus.Success -> SuccessSearchResultScreen(
                    modifier = Modifier
                        .padding(paddingValues),
                    navController = navController,
                    searchResultState = searchResultState.value,
                    getSearchResult = {
                        viewModel.takeSearchResult(it.products)
                    },
                    saveGift = {
                        viewModel.saveGift(SavedGifts(
                            giftName = it.name,
                            giftUrl = it.url,
                            giftImage = it.imageUrl ?: "",
                        ))
                    },
                    context = context
                )

                QueryProductsStatus.None -> {

                }
            }
        }
    }

}

@Composable
fun SuccessSearchResultScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    getSearchResult : ()-> Unit,
    saveGift : (SearchCardModel)-> Unit,
    searchResultState: SearchResultStatus,
    context: Context
) {
    BackHandler(enabled = true) {
        navController.navigate(LocalNavController.MainScreen){
            popUpTo(0) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        //GetSearchResult
        getSearchResult()
    }
        Box(
            modifier = modifier
                .fillMaxSize()
        ){
            searchResultState.let {
                when(it) {
                    is SearchResultStatus.Error ->  {
                        Text(
                            text = it.message,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    SearchResultStatus.Loading ->  {
                        LoadingScreen(Modifier.fillMaxSize())
                    }

                    is SearchResultStatus.Success ->{
                        LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(120.dp,),
                            state = rememberLazyGridState(),
                            horizontalArrangement = Arrangement.spacedBy(25.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(horizontal = 20.dp)
                        ) {
                            items(it.searchCardModels) { card ->
                                val giftSaved = remember {
                                    mutableStateOf(false)
                                }
                                SearchCard(
                                    modifier = Modifier,
                                    searchCardModel = card,
                                    isSaved = giftSaved.value,
                                    onBuyButton = {
                                        val intent = Intent(Intent.ACTION_VIEW, card.url.toUri())
                                        context.startActivity(intent)
                                    },
                                    onSaveButton = {
                                        if (!giftSaved.value){
                                            giftSaved.value = true
                                            saveGift(card)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    SearchResultStatus.None -> {

                    }
                }
            }

        }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuccessSearchResultScreen(
        modifier = Modifier,
        navController = rememberNavController(),
        searchResultState = SearchResultStatus.Success(listOf<SearchCardModel>(
            SearchCardModel(
                name = "Ürün Adı",
                price = "100 TL",
                imageUrl = "https://example.com/product.jpg",
                url = "https://example.com/product"
            ),
            SearchCardModel(
                name = "Ürün Adı",
                price = "100 TL",
                imageUrl = "https://example.com/product.jpg",
                url = "https://example.com/product"
            ),
            SearchCardModel(
                name = "Ürün Adı",
                price = "100 TL",
                imageUrl = "https://example.com/product.jpg",
                url = "https://example.com/product"
            ),
            SearchCardModel(
                name = "Ürün Adı",
                price = "100 TL",
                imageUrl = "https://example.com/product.jpg",
                url = "https://example.com/product"
            ),
            SearchCardModel(
                name = "Ürün Adı",
                price = "100 TL",
                imageUrl = "https://example.com/product.jpg",
                url = "https://example.com/product"
            ),)
        ),
        getSearchResult = {

        },
        saveGift = {

        },
        context = LocalContext.current
    )
}