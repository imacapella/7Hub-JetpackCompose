package com.example.myapplication.Views.AccountView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.R
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.LoginView.LoginViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow

@Composable
fun AccountScreen(
    viewModel: AccountViewModel = viewModel(),
    loginViewModel: LoginViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Dialog state'ini tutmak için
    var showDialog by remember { mutableStateOf(false) }

    // Alert Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = "Change Password",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to change your password?",
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate("resetpw")
                    }
                ) {
                    Text(
                        text = "Yes",
                        color = Constants.hubGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text(
                        text = "No",
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Title Circle
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
        ) {
            val circleRadius = 800.dp.toPx()
            val circleCenterX = size.width / 2
            val circleCenterY = -circleRadius + 570F

            drawCircle(
                color = Constants.hubBlue,
                radius = circleRadius,
                center = Offset(circleCenterX, circleCenterY)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // Back Button and Title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Account",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
                // Boş box ile dengeleme
                Box(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(70.dp))

            // Profile Picture
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                contentAlignment = Alignment.Center
            ) {
                // Gölge efekti için arka plan
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .shadow(
                            elevation = 20.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.3f),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                )
                
                // Profil fotoğrafı
                AsyncImage(
                    model = uiState.photoUrl.takeIf { !it.isNullOrEmpty() }
                        ?: R.drawable.profilepictureanonim,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.profilepictureanonim),
                    placeholder = painterResource(id = R.drawable.profilepictureanonim)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Menu Items
            MenuButton(
                text = "Personal Info",
                icon = Icons.Default.Person,
                onClick = { /* Navigate to Personal Info */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                text = "Change Password",
                icon = Icons.Default.Lock,
                onClick = { showDialog = true }
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                text = "Help",
                icon = Icons.Default.Help,
                onClick = { navController.navigate("help") }
            )

            Spacer(modifier = Modifier.height(100.dp))  // Sadece 32.dp'lik sabit bir boşluk

            // Log Out Button
            Button(
                onClick = {
                    viewModel.signOut()
                    loginViewModel.signout()
                    onSignOut()
                },
                modifier = Modifier
                    .width(144.dp)
                    .height(58.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5BC658)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Log Out",
                    color = Constants.hubWhite,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF85C0FF)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountScreenPreview() {
    val previewNavController = rememberNavController()
    val previewAccountViewModel: AccountViewModel = viewModel()
    val previewLoginViewModel: LoginViewModel = viewModel()
    
    MaterialTheme {
        AccountScreen(
            viewModel = previewAccountViewModel,
            loginViewModel = previewLoginViewModel,
            onNavigateBack = {},
            onSignOut = {},
            navController = previewNavController
        )
    }
}
