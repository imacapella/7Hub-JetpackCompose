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
import androidx.compose.material.icons.filled.ArrowForward
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
import com.example.myapplication.R
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
            val circleCenterY = -circleRadius + 230f

            drawCircle(
                color = Constants.hubBlue,
                radius = circleRadius,
                center = Offset(circleCenterX, circleCenterY)
            )
        }

        // Back button
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
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Constants.hubGreen
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(150.dp))  // TopBar'dan sonra boşluk

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
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Rating stars
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
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

                    // Course Name
                    Text(
                        text = displayState.courseName,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Constants.hubDark
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description
                    if (displayState.courseDesc.isNotEmpty()) {
                        Text(
                            text = displayState.courseDesc,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            lineHeight = 24.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))



                    // Instructor section
                    if (displayState.instructor.isNotEmpty()) {
                        Text(
                            text = "Instructor:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            AsyncImage(
                                model = displayState.instructorImageUrl.ifEmpty { 
                                    R.drawable.app_logo
                                },
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

                    Spacer(modifier = Modifier.height(32.dp))

                    // Chat Button
                    Button(
                        onClick = onChatClick,
                        modifier = Modifier
                            .width(110.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Constants.hubGreen
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Chat",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
