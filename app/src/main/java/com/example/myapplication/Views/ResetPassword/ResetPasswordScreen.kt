package com.example.myapplication.Views.ResetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Components.CustomButton
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.LoginView.HeadCircle
import com.example.myapplication.Views.LoginView.WelcomeSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ResetPasswordViewModel
) {
    val email by viewModel.email.collectAsState()
    val isEmailSent by viewModel.isEmailSent.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadCircle()
            
            if (!isEmailSent) {
                WelcomeSection()
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    buttonText = "Send Code",
                    buttonTextColor = Constants.hubWhite,
                    buttonIcon = Icons.Default.ChevronRight,
                    onClick = {
                        viewModel.sendResetEmail()
                    }
                )
            } else {
                Text(
                    text = "Mail adresinize gönderilen linkten şifre değiştirme işlemine devam ediniz.",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Constants.hubDark,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                CustomButton(
                    buttonColor = Constants.hubBabyBlue,
                    buttonText = "Giriş Ekranına Dön",
                    buttonTextColor = Constants.hubWhite,
                    buttonIcon = Icons.Default.ArrowBack,
                    onClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }

            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
