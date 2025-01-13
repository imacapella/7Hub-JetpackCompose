package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.DataLayer.Models.CourseModel
import com.example.myapplication.R
import com.example.myapplication.Utilities.Constants

// Composable Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    viewModel: CoursesViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onCourseClick: (CourseModel) -> Unit = {}
) {
    Log.d("CoursesScreen", "Screen composing")
    
    val courses by viewModel.courses.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()

    Log.d("CoursesScreen", "Current tab: $selectedTab")
    Log.d("CoursesScreen", "Number of courses: ${courses.size}")
    courses.forEach { course ->
        Log.d("CoursesScreen", "Course: ${course.Identifier} - ${course.courseName}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Constants.hubWhite)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Courses",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 100.dp) // Başlığı sağa kaydırmak için padding
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(0xFFF3F3F3)
            )
        )

        // Tabs
        TabsRow(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                Log.d("CoursesScreen", "Tab selected: $tab")
                viewModel.onTabSelected(tab)
            }
        )

        // Course List
        if (courses.isEmpty()) {
            Log.d("CoursesScreen", "No courses to display")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Log.d("CoursesScreen", "Displaying ${courses.size} courses")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(courses) { course ->
                    Log.d("CoursesScreen", "Rendering course: ${course.Identifier} - ${course.courseName}")
                    CourseCard(
                        course = course,
                        onClick = { 
                            Log.d("CoursesScreen", "Course clicked: ${course.Identifier}")
                            onCourseClick(course)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .width(180.dp),  // Her iki buton için sabit genişlik
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                Log.d("ButtonClicked", "$text button clicked")
                onClick()
            },
            modifier = Modifier
                .height(if (selected) 46.dp else 42.dp)  // Aktif buton daha büyük
                .fillMaxWidth(),  // Box'ın genişliğini tamamen doldur
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F3F3) // Arka plan rengi
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,  // Gölge efekti
                pressedElevation = 6.dp
            ),
            shape = RoundedCornerShape(10.dp)  // Butonun köşelerini yuvarla
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = Color(0xFF718A39),  // Yazı rengi
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun TabsRow(
    selectedTab: CourseTab,
    onTabSelected: (CourseTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TabButton(
            text = "My Courses",
            selected = selectedTab == CourseTab.MY_COURSES,
            onClick = { onTabSelected(CourseTab.MY_COURSES) }
        )
        Spacer(modifier = Modifier.width(16.dp))
        TabButton(
            text = "All Courses",
            selected = selectedTab == CourseTab.ALL_COURSES,
            onClick = { onTabSelected(CourseTab.ALL_COURSES) }
        )
    }
}

@Preview
@Composable
fun myPreview(){
    CoursesScreen()
}
