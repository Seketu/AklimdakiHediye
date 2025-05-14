package com.reylortechnology.aklimdakihediye.Views.PartScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.AdMob.AdBanner
import com.reylortechnology.aklimdakihediye.Compose.SearchCard
import com.reylortechnology.aklimdakihediye.Compose.StepperIndicator
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.R


@Composable
fun SearchResultScreen(
    viewModel: AboutGiftInformationViewModel = hiltViewModel(),
    navController: NavController
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
    navController: NavController,
    viewModel: AboutGiftInformationViewModel,
    products: List<Products>
) {
    val searchResultState = viewModel.totalState.collectAsState()
    val pagerState = rememberPagerState(pageCount = {
        (searchResultState.value as? SearchResultStatus.Success)?.searchWithLabelList?.size ?: 0
    })

    BackHandler(enabled = true) {
        navController.navigate(LocalNavController.MainScreen){
            popUpTo(0) { inclusive = true }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.takeSearchResult(products)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(all = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.home_button),
                    contentDescription = "Home Button",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable {
                            navController.navigate(LocalNavController.MainScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            searchResultState.value.let {
                when (it) {
                    is SearchResultStatus.Error -> {
                        Text(text = "Hata Oluştu")
                    }

                    is SearchResultStatus.Loading -> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }

                    is SearchResultStatus.Success -> {
                        val list = it.searchWithLabelList
                        AdBanner(modifier = Modifier.fillMaxWidth())
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize(),
                        ) { pageIndex ->
                            val labelGroup = list[pageIndex]

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                item {
                                    StepperIndicator(
                                        pageIndex + 1,
                                        Modifier
                                            .fillMaxWidth()
                                            .height(40.dp),
                                        list.size
                                    )
                                }

                                item {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(0.9f)
                                            .padding(vertical = 12.dp, horizontal = 15.dp),
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = labelGroup.label,
                                            fontSize = 30.sp,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = FontFamily.SansSerif
                                        )
                                    }
                                }

                                items(labelGroup.searchCardItems) { item ->
                                    SearchCard(
                                        modifier = Modifier
                                            .padding(horizontal = 25.dp, vertical = 10.dp)
                                            .fillMaxWidth(),
                                        item = item,
                                        saveGift = {
                                            val saveGift = SavedGifts(
                                                giftName = item.name,
                                                giftUrl = item.url,
                                                giftDescription = item.description
                                            )
                                            viewModel.saveGift(saveGift)
                                        }
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SearchResultScreen(navController = rememberNavController())
}