package com.sparkathon.shopmate.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.sparkathon.shopmate.ui.theme.ShopMateTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopMateTheme {
                MainScreen()
            }
        }
    }
}
