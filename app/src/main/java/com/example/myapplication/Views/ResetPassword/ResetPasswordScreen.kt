package com.example.myapplication.Views.ResetPassword

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import android.content.Context
import android.util.TypedValue
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Components.CustomButton
import com.example.myapplication.R
import com.example.myapplication.Utilities.Constants

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
    ) {
        ResetPasswordHeadCircle(navController = navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isEmailSent) {
                ResetPasswordWelcomeSection()
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = viewModel::onEmailChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(8.dp),
                    shape = RoundedCornerShape(20.dp),
                    placeholder = { Text("Enter your email", color = Constants.hubGray) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Constants.hubGray
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Constants.hubGray,
                        unfocusedBorderColor = Constants.hubGray,
                        cursorColor = Constants.hubGray,
                    ),
                    singleLine = true,
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
                    buttonColor = Constants.hubAcikYesil,
                    buttonText = "Return to Login",
                    buttonTextColor = Constants.hubWhite,
                    buttonIcon = Icons.Default.ArrowBack,
                    modifier = Modifier
                        .fillMaxWidth(),  // genişliğin %85'ini kaplayacak
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

@Composable
fun ResetPasswordWelcomeSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Forgot Your Password?",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 35.sp,
            color = Constants.hubDarkGray,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your email address below, and we'll send you a link to reset your password.",
            fontSize = 16.sp,
            color = Constants.hubDarkGray,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ResetPasswordHeadCircle(navController: NavController) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp)
    ) {
        // Head Circle
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawTitleCircle(this, density)
        }

        // Back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Constants.hubWhite
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reset Password",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Constants.hubWhite
            )
        }
    }
}

fun drawTitleCircle(scope: DrawScope, density: Density) {
    with(density) {
        val circleRadius = 800.dp.toPx()
        val circleCenterX = scope.size.width / 2
        val circleCenterY = -circleRadius + 500f

        scope.drawCircle(
            color = Constants.hubBlue,
            radius = circleRadius,
            center = Offset(circleCenterX, circleCenterY)
        )
    }
}
