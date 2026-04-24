package com.reylortechnology.aklimdakihediye.AdMob

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.reylortechnology.aklimdakihediye.Views.PartScreens.LoadingScreen
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError


// test kimliği : ca-app-pub-3940256099942544/1033173712
// uygulama kimliği : ca-app-pub-3865288813123224/9193054578
@Composable
fun InterstitialAdScreen(
    onAdClosed: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = remember(context) {
        context as? Activity
    }

    var interstitialAd by remember { mutableStateOf<InterstitialAd?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        MobileAds.initialize(context) {} // SDK başlatma

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            "ca-app-pub-3865288813123224/9193054578",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false

                    ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Log.d("AdMob", "Ad dismissed.")
                            onAdClosed()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Log.d("AdMob", "Ad failed to show: ${adError.message}")
                            onAdClosed()
                        }
                    }

                    activity?.let {
                        ad.show(it)
                    } ?: run {
                        Log.e("AdMob", "Activity is null, cannot show ad.")
                        onAdClosed()
                    }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.e("AdMob", "Failed to load interstitial ad: ${loadAdError.message}")
                    isLoading = false
                    onAdClosed()
                }
            }
        )
    }

    // Reklam yüklenirken gösterilecek basit loading ekranı
    if (isLoading) {
        LoadingScreen()
    }
}
