package com.example.myapplication.Views.LoginView

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Components.CustomButton
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.ResetPassword.ResetPasswordViewModel

@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val authState by loginViewModel.authState.observeAsState()
    val context = LocalContext.current

    // Auth durumuna göre işlem yap
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("home") {
                    // Login ekranına geri dönüşü engelle
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(
                    context,
                    (authState as AuthState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                // Hata mesajından sonra state'i sıfırla
                loginViewModel.checkAuthStatus()
            }
            else -> Unit
        }
    }

    // UI tanımı (kalan kısım değişmeyecek)
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
        ) {
            HeadCircle()
            WelcomeSection()
            Spacer(modifier = Modifier.height(16.dp))

            UserNameView(
                value = email,
                onValueChange = { email = it })

            Spacer(modifier = Modifier.height(8.dp))
            PasswordView(value = password, onValueChange = {password = it})

            Spacer(modifier = Modifier.height(16.dp))

            ForgotPasswordText {
                navController.navigate("resetpw")
            }

            Spacer(modifier = Modifier.height(40.dp))
            CustomButton(

                buttonText = "Log In",
                buttonTextColor = Constants.hubWhite,
                buttonIcon = Icons.Filled.Login,

            ) {
                loginViewModel.login(email, password)
            }
        }
    }
}

@Composable
fun HeadCircle() {
    val circleRadius = 800.dp // Dairenin yarıçapı

    Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
        // Dairenin merkezini değiştirelim
        val circleCenterX = size.width / 2 // Yatayda ekranın ortasında
        val circleCenterY = -circleRadius.toPx() + 50f // Dikeyde daha yukarıya kaydırdık

        // Dairenin çizilmesi
        drawCircle(
            color = Constants.hubBlue, // Dairenin rengi
            radius = circleRadius.toPx(), // Yarıçap
            center = Offset(circleCenterX, circleCenterY) // Dairenin merkezi
        )
    }
}

//sasa
@Composable
fun WelcomeSection(){
    Text(
        text = "Hello Student!",
        style = MaterialTheme.typography.headlineMedium,
        fontSize = 35.sp,
        color = Constants.hubDarkGray,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Start,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
        text = "Log in with your OBS information",
        fontSize = 16.sp,
        color = Constants.hubDarkGray,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserNameView(value: String, onValueChange: (String) -> Unit) {
    val customColor = Constants.hubGray

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        placeholder = { Text("Enter your username", color = Constants.hubGray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Username Icon",
                tint = customColor
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = customColor,
            unfocusedBorderColor = customColor,
            cursorColor = customColor,
            //textColor = Color.Black // Yazının siyah olması için
        ),
        singleLine = true,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordView(value: String, onValueChange: (String) -> Unit) {
    val customColor = Constants.hubGray

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(20.dp))
            .padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        placeholder = { Text("Enter your password", color = Constants.hubGray) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = "Password Icon",
                tint = customColor
            )
        },
        singleLine = true,
        textStyle = TextStyle(color = Constants.hubDark), // Yazı rengini siyah yapıyoruz
        visualTransformation = PasswordVisualTransformation(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = customColor,
            unfocusedBorderColor = customColor,
            cursorColor = customColor,
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun ForgotPasswordText(onClick: () -> Unit) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Constants.hubAcikYesil,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append("I forgot my password")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        textAlign = TextAlign.End
    )
}

/*@Preview
@Composable
fun MyPri(){
    val navController = rememberNavController()
    
    LoginPage(
        navController,
        viewModel = LoginViewModel()
    )
}*/