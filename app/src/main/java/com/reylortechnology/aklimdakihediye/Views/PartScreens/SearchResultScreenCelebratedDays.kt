package com.reylortechnology.aklimdakihediye.Views.PartScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.Compose.SearchCard
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ViewModels.CelebretadDayViewModel
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus


//Todo update all search screen
@Composable
fun SearchResultScreenCelebratedDays(
    viewModel: CelebretadDayViewModel = hiltViewModel(),
    navController: NavController
) {

    val queryState = viewModel.queryState.collectAsState()
    val searchResultState = viewModel.totalState.collectAsState()
    queryState.value.let {
        when (it) {
            is QueryProductsStatus.Error -> Text(
                text = it.message,
                fontSize = 20.sp,
                fontFamily = FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            is QueryProductsStatus.Loading -> LoadingScreen()
            is QueryProductsStatus.Success -> SuccessSearchResultScreen(
                navController = navController,
                searchResultState = searchResultState,
                getSearchResult = {
                    viewModel.takeSearchResult(it.products)
                }
            )
        }
    }
}

@Composable
fun SuccessSearchResultScreen(
    navController: NavController,
    getSearchResult : ()-> Unit,
    searchResultState: State<SearchResultStatus>
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

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ){
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                state = rememberLazyGridState(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                searchResultState.value.let {
                    when(it) {
                        is SearchResultStatus.Error -> item {
                            Text(
                                text = it.message,
                                fontSize = 20.sp,
                                fontFamily = FontFamily.SansSerif,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        SearchResultStatus.Loading -> item {
                            LoadingScreen()
                        }

                        is SearchResultStatus.Success -> items(it.searchCardModels) { card ->
                            SearchCard(
                                modifier = Modifier,
                                searchCardModel = card,
                                onBuyButton = {},
                                onSaveButton = {}
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SearchResultScreenCelebratedDays(navController = rememberNavController())
}