package com.gmail.uli153.ulisestv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.tv.material3.Surface
import com.gmail.uli153.ulisestv.ui.navigation.UlisesTvNavHost
import com.gmail.uli153.ulisestv.ui.theme.UlisesTvTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UlisesTvTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    UlisesTvNavHost()
                }
            }
        }
    }
}
