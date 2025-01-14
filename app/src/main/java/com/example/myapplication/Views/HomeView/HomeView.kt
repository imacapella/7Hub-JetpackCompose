package com.example.myapplication.Views

import androidx.compose.foundation.Canvas
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.LoginView.AuthState
import com.example.myapplication.Views.LoginView.LoginViewModel
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.Views.HomeView.HomeViewModel
import com.example.myapplication.Views.HomeView.MyCoursesSection

@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    navController: NavController,
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val authState by loginViewModel.authState.observeAsState()
    val userName by homeViewModel.userName.collectAsState()
    val studentId by homeViewModel.studentId.collectAsState()
    val courses by homeViewModel.courses.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Unauthenticated -> {
                if (navController.currentDestination?.route != "login") {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
            else -> Unit
        }
    }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF3F3F3))
                .padding(12.dp)
        ) {
            TitleCircle()
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = "Hey, ${userName?.split(" ")?.firstOrNull() ?: "Student"}!",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        Text(
            text = studentId ?: "",
            fontSize = 16.sp,
            color = Color(0xFF88B04B),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        SearchBar()
        Spacer(modifier = Modifier.height(25.dp))

        CategorySection(navController)
        Spacer(modifier = Modifier.height(12.dp))

        MyCoursesWithNavButton(navController)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            MyCoursesSection(courses = courses, navController)
        }
    }
}

@Composable
fun TitleCircle() {
    val circleRadius = 800.dp

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(60.dp)
    ) {
        val circleCenterX = size.width / 2
        val circleCenterY = -circleRadius.toPx() + 150f

        drawCircle(
            color = Constants.hubBlue,
            radius = circleRadius.toPx(),
            center = Offset(circleCenterX, circleCenterY)
        )
    }
}

@Composable
fun MyCoursesWithNavButton(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "My Courses",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 26.sp,
            color = Color(0xFF342E37),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        IconButton(
            onClick = { navController.navigate("courses") }
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate to Courses",
                tint = Constants.hubBabyBlue
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }

    TextField(
        value = searchText,
        onValueChange = { searchText = it },
        placeholder = { Text(
            text = "What are you looking for?",
            color = Constants.hubGreen) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Constants.hubGreen
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Constants.hubGreen,
            unfocusedIndicatorColor = Constants.hubDark,
            containerColor = Constants.hubWhite
        )
    )
}

@Composable
fun CategoryCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 118.dp, height = 190.dp)
            .shadow(5.dp, shape = RoundedCornerShape(16.dp))
            .padding(4.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Constants.hubWhite,
                    modifier = Modifier.size(65.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Navigate",
                        tint = Constants.hubBabyBlue,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun CategorySection(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        CategoryCard(
            title = "Courses",
            icon = Icons.Default.Book,
            backgroundColor = Constants.hubBlue,
            onClick = { navController.navigate("courses") }
        )
        CategoryCard(
            title = "Groups",
            icon = Icons.Default.Group,
            backgroundColor = Constants.hubBabyBlue,
            onClick = { navController.navigate("groups") }
        )
        CategoryCard(
            title = "Clubs",
            icon = Icons.Default.People,
            backgroundColor = Constants.hubBabyBlue,
            onClick = { navController.navigate("clubs") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview() {
    val previewNavController = rememberNavController()
    val previewLoginViewModel = LoginViewModel()
    
    HomeView(
        navController = previewNavController,
        loginViewModel = previewLoginViewModel
    )
}
