package com.example.myapplication

import SplashScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Views.LoginView.LoginViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var showSplash by remember { mutableStateOf(true) }
    val loginViewModel: LoginViewModel = viewModel()

    if (showSplash) {
        SplashScreen {
            showSplash = false
        }
    } else {
        MainScreen(loginViewModel)
    }
}
