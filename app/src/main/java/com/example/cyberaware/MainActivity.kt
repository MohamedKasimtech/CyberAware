package com.example.cyberaware

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cyberaware.navigation.AppNavigation
import com.example.cyberaware.ui.theme.CyberAwareTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CyberAwareTheme {
                AppNavigation()
            }
        }
    }
}
