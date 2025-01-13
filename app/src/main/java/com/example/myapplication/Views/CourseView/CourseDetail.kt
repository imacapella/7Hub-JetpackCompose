package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.Utilities.Constants

@Composable
fun CourseDetailScreen(
    viewModel: CourseDetailViewModel,
    courseCode: String,
    onBackClick: () -> Unit = {},
    onChatClick: () -> Unit = {}
) {
    Log.d("CourseDetailScreen", "Screen composing with course code: $courseCode")

    LaunchedEffect(courseCode) {
        Log.d("CourseDetailScreen", "Loading course details for code: $courseCode")
        viewModel.loadCourseDetails(courseCode)
    }

    val uiState by viewModel.uiState.collectAsState()
    Log.d("CourseDetailScreen", "Current UI state: $uiState")

    // Remember the last valid state
    var lastValidState by remember { mutableStateOf<CourseDetailUiState?>(null) }
    
    // Update lastValidState when we have valid data
    LaunchedEffect(uiState) {
        if (!uiState.isLoading && uiState.courseName.isNotEmpty()) {
            lastValidState = uiState
        }
    }

    // Use either the current state or last valid state
    val displayState = lastValidState ?: uiState

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Circle
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
        ) {
            val circleRadius = 800.dp.toPx()
            val circleCenterX = size.width / 2
            val circleCenterY = -circleRadius + 210f

            drawCircle(
                color = Constants.hubBabyBlue,
                radius = circleRadius,
                center = Offset(circleCenterX, circleCenterY)
            )
        }

        // Back button - Always visible
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Geri",
                tint = Color.White
            )
        }

        when {
            uiState.isLoading && lastValidState == null -> {
                Log.d("CourseDetailScreen", "Showing loading indicator")
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Constants.hubGreen)
                }
            }
            else -> {
                Log.d("CourseDetailScreen", "Showing course details")
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(60.dp))

                    // Course code badge
                    Surface(
                        color = Constants.hubGreen,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(157.dp)
                            .height(58.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = displayState.identifier,
                                color = Constants.hubWhite,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 28.sp
                                ),
                                modifier = Modifier
                                    .width(129.dp)
                                    .height(35.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating stars
                    Row(
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (index < displayState.courseRating) Constants.hubGreen else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Course details
                    Text(
                        text = displayState.courseName,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Course Description
                    if (displayState.courseDesc.isNotEmpty()) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = displayState.courseDesc,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Credits
                    Text(
                        text = "Credits: ${displayState.courseCredit}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instructor Name
                    if (displayState.instructor.isNotEmpty()) {
                        Text(
                            text = "Instructor: ${displayState.instructor}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Semester
                    if (displayState.semester.isNotEmpty()) {
                        Text(
                            text = "Semester: ${displayState.semester}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Prerequisites
                    if (displayState.prerequisites.isNotEmpty()) {
                        Text(
                            text = "Prerequisites",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        displayState.prerequisites.forEach { prerequisite ->
                            Text(
                                text = "• $prerequisite",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Syllabus
                    if (displayState.syllabus.isNotEmpty()) {
                        Text(
                            text = "Syllabus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = displayState.syllabus,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Announcements
                    if (displayState.announcements.isNotEmpty()) {
                        Text(
                            text = "Announcements",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        displayState.announcements.forEach { announcement ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Text(
                                    text = announcement,
                                    modifier = Modifier.padding(8.dp),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Course Actions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { 
                                Log.d("CourseDetailScreen", "Join chat button clicked")
                                onChatClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Constants.hubGreen
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sohbete Katıl")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Instructor section with image
                    if (displayState.instructor.isNotEmpty()) {
                        Text(
                            text = "Instructor:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            AsyncImage(
                                model = displayState.instructorImageUrl,
                                contentDescription = "Instructor image",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Text(
                                text = displayState.instructor,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp),
                                color = Constants.hubGreen
                            )
                        }

                    }
                }
            }
        }
    }
}
