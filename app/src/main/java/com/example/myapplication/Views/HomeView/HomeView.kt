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
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
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
import com.example.myapplication.DataLayer.Models.CourseModel
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.LoginView.AuthState
import com.example.myapplication.Views.LoginView.LoginViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeView(modifier: Modifier = Modifier, navController: NavController, loginViewModel: LoginViewModel) {

    val authState by loginViewModel.authState.observeAsState()

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

    val courses = listOf(
        CourseModel("VCD 471", "Interactive Design Studio", "Merve Çaşkurlu"),
        CourseModel("VCD 592", "Internship", "Murat Yilmaz"),
        CourseModel("VCD 123", "Introduction to Design", "Ayşe Yavuz"),
        CourseModel("VCD 345", "Sound Studio", "Ali Gür")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
            .padding(12.dp)
    ) {
        TitleCircle()
        Text(
            text = "Hey, Alice!",
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = "20212345678",
            fontSize = 16.sp,
            color = Color(0xFF88B04B),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextButton(onClick = {
            loginViewModel.signout()
        }) {
            Text(text = "Sign out")
        }

        SearchBar()
        Spacer(modifier = Modifier.height(15.dp))

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
            color = Color(0xFF1E3A5F),
            radius = circleRadius.toPx(),
            center = Offset(circleCenterX, circleCenterY)
        )
    }
}

@Composable
fun MyCoursesSection(courses: List<CourseModel>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(courses) { course ->
            CourseCard(
                courseCode = course.courseCode,
                courseTitle = course.courseTitle,
                instructorName = course.instructorName,
                onClick = {
                    navController.navigate("course_detail/${course.courseCode}")
                }
            )
        }
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
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Navigate to Courses",
                tint = Color(0xFF9EC7F2)
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
        placeholder = { Text("What are you looking for?") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
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
            containerColor = Constants.hubDark
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
                tint = Color.White,
                modifier = Modifier.size(65.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CourseCard(
    courseCode: String,
    courseTitle: String,
    instructorName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(350.dp)
            .height(250.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Course code section with light blue background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
                    .background(
                        color = Color(0xFF9EC7F2),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = courseCode,
                    fontSize = 55.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // Course title and instructor name
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = courseTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF342E37)
                )
                Text(
                    text = instructorName,
                    fontSize = 14.sp,
                    color = Color(0xFF718A39)
                )
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
            backgroundColor = Color(0xFF1E3A5F),
            onClick = { navController.navigate("courses") }
        )
        CategoryCard(
            title = "Groups",
            icon = Icons.Default.Group,
            backgroundColor = Color(0xFF4285F4),
            onClick = { navController.navigate("groups") }
        )
        CategoryCard(
            title = "Clubs",
            icon = Icons.Default.People,
            backgroundColor = Color(0xFF4285F4),
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
