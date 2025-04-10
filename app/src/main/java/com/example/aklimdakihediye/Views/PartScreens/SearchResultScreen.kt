package com.example.aklimdakihediye.Views.PartScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.aklimdakihediye.Compose.SearchCard
import com.example.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.example.aklimdakihediye.models.GiftInformationModels.Products
import com.example.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.example.aklimdakihediye.ObserverClasses.SearchResultStatus


@Composable
fun SearchResultScreen(
    modifier: Modifier = Modifier,
    viewModel: AboutGiftInformationViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val queryState = viewModel.queryState.collectAsState()

    queryState.value.let {
        when (it) {
            is QueryProductsStatus.Error -> TODO()
            is QueryProductsStatus.Loading -> LoadingScreen()
            is QueryProductsStatus.Success -> SuccesSearchResultScreen(
                navController = navController,
                viewModel = viewModel,
                it.products
            )
        }
    }
}

@Composable
fun SuccesSearchResultScreen(
    navController: NavHostController,
    viewModel: AboutGiftInformationViewModel,
    products: List<Products>
) {

    val searchResultState = viewModel.totalState.collectAsState()

    LaunchedEffect(
        Unit
    ) {
        viewModel.takeSearchResult(products)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Harika İşte Senin Durumun İçin Harika Öneriler"
            )
            LazyColumn {
                searchResultState.value.let {
                    when (it) {
                        is SearchResultStatus.Error -> {
                            item {
                                Text(text = "Hata Oluştu")
                            }
                        }
                        is SearchResultStatus.Loading -> {
                            item {
                                LoadingScreen()
                            }
                        }
                        is SearchResultStatus.Success -> {
                            it.searchWithLabelList.forEach { searchWithLabel ->
                                item {
                                    Text(searchWithLabel.label)
                                }
                                items(searchWithLabel.searchCardItems) {
                                    SearchCard(
                                        modifier = Modifier,
                                        item = it
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}