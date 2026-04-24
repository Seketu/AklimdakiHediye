package com.reylortechnology.aklimdakihediye.AdMob

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

//Mutlaka burada id tanımını değiştir
// asıl kimlik : ca-app-pub-3865288813123224/9603268055
// test kimliği : ca-app-pub-3940256099942544/9214589741
@Composable
fun AdBanner(modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            AdView(it).apply {
                setAdSize(AdSize.BANNER)
                loadAd(AdRequest.Builder().build())
                adUnitId = "ca-app-pub-3865288813123224/9603268055"
            }
        }
    )
}