package com.example.myapplication.Views.HomeView

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.DataLayer.Models.CourseModel
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.CourseView.CourseCard


@Composable
fun MyCoursesSection(
    courses: List<CourseModel>,
    navController: NavController
) {
    Log.d("MyCoursesSection", "Composing with ${courses.size} courses")
    courses.forEach { course ->
        Log.d("MyCoursesSection", "Course in list: ${course.Identifier} - ${course.courseName}")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        if (courses.isEmpty()) {
            Log.d("MyCoursesSection", "No courses to display")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Constants.hubGreen)
            }
        } else {
            Log.d("MyCoursesSection", "Displaying courses in LazyRow")
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(courses) { course ->
                    Log.d("MyCoursesSection", "Rendering course card: ${course.Identifier}")
                    CourseCard(
                        course = course,
                        onClick = { 
                            Log.d("MyCoursesSection", "Course clicked: ${course.Identifier}")
                            navController.navigate("course_detail/${course.Identifier}") 
                        }
                    )
                }
            }
        }
    }
}
