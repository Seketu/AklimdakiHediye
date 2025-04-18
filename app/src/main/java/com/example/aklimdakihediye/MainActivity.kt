package com.example.aklimdakihediye

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ui.theme.AklimdakiHediyeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) // daima açık tema (light mode)
        setContent {
            AklimdakiHediyeTheme {
                LocalNavController().LocalNavHost()
            }
        }
    }
}
