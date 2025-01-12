package com.example.myapplication.Views.CourseView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.sp
import com.example.myapplication.Utilities.Constants

@Composable
fun CourseDetailScreen(
    viewModel: CourseDetailViewModel,
    courseCode: String,
    onBackClick: () -> Unit,
    onChatClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Remember the last successful state
    var lastValidState by remember { mutableStateOf<CourseDetailUiState?>(null) }
    
    // Update lastValidState when we have valid data
    LaunchedEffect(uiState) {
        if (!uiState.isLoading && uiState.error == null && uiState.courseTitle.isNotEmpty()) {
            lastValidState = uiState
        }
    }

    // Use either the current state or last valid state
    val displayState = if (!uiState.isLoading && uiState.error == null && uiState.courseTitle.isNotEmpty()) {
        uiState
    } else {
        lastValidState ?: uiState
    }

    LaunchedEffect(courseCode) {
        viewModel.loadCourseDetails(courseCode)
    }

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

        when {
            displayState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Constants.hubGreen)
                }
            }
            displayState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = displayState.error ?: "Bir hata oluştu",
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Back button
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.align(Alignment.Start)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }

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
                                text = displayState.courseCode,
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
                                tint = if (index < displayState.rating) Constants.hubGreen else Color.Gray,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Course details
                    Text(
                        text = displayState.courseTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Course Description
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = displayState.courseDescription ?: "No description available",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Credits
                    Text(
                        text = "Credits: ${displayState.credits ?: "N/A"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Instructor Name
                    displayState.instructorName?.let { name ->
                        Text(
                            text = "Instructor: $name",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Semester
                    displayState.semester?.let { sem ->
                        Text(
                            text = "Semester: $sem",
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
                    displayState.syllabus?.let { syl ->
                        if (syl.isNotEmpty()) {
                            Text(
                                text = "Syllabus",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = syl,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
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

                    // Instructor section
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
                        displayState.instructorName?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 16.dp),
                                color = Constants.hubGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Chat button
                    Button(
                        onClick = onChatClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Constants.hubGreen
                        )
                    ) {
                        Text(
                            text = "Sohbete Git",
                            style = MaterialTheme.typography.titleMedium,
                            color = Constants.hubWhite
                        )
                    }

                    // Admin Tools
                    if (displayState.userRole == "admin") {
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Button(
                            onClick = { viewModel.updateInstructorData() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            )
                        ) {
                            Text(
                                text = "Update Database",
                                style = MaterialTheme.typography.titleMedium,
                                color = Constants.hubWhite
                            )
                        }

                        // Update Message
                        displayState.updateMessage?.let { message ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (message.contains("başarısız")) Color.Red else Color(0xFF4CAF50),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // Loading Indicator
                        if (displayState.isUpdating) {
                            Spacer(modifier = Modifier.height(8.dp))
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }
        }
    }
}
