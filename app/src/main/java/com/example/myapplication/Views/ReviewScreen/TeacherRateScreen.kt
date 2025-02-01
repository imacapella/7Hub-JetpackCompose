package com.example.myapplication.Views.ReviewScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.ui.theme.CustomShadowButton
import androidx.compose.foundation.border



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateTeacherScreen(
    teacher: Teacher,
    onSubmitReview: (Review) -> Unit,
    onNavigateBack: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFFF3F3F3),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = 100.dp, y = 11.dp)
                    ) {
                        Text(
                            text = "Reviews",
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 25.sp,
                                color = Color(0xFF5F5464)
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(id = com.example.myapplication.R.drawable.ic_arrow_left),
                            contentDescription = "Back",
                            tint = Color(0xFF3C9BFF)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF3F3F3))
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Teacher Photo, Total Rating and Stars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = teacher.photo),
                    contentDescription = "Teacher Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = teacher.name,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating Section
                    Row {
                        repeat(5) { index ->
                            val starIcon = if (index < rating) {
                                painterResource(id = com.example.myapplication.R.drawable.ic_star_filled)
                            } else {
                                painterResource(id = com.example.myapplication.R.drawable.ic_star_outline)
                            }
                            Icon(
                                painter = starIcon,
                                contentDescription = "Rating Star",
                                tint = Color(0xFF5BC658),
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        rating = index + 1
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Comment Form (325x166 layout)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(166.dp)
                    .padding(16.dp)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp)
            ) {
                BasicTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    textStyle = TextStyle(fontSize = 14.sp),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    onSubmitReview(Review(0, "Anonim", comment, rating, teacher.id))
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5BC658))
            ) {
                Text(
                    text = "Submit",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }
        }
    }
}