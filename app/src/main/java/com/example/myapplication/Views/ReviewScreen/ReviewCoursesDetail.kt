package com.example.myapplication.Views.ReviewScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import androidx.compose.runtime.*
import com.example.myapplication.ui.theme.CustomShadowButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.background

@Composable
fun CourseReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Anonim", // Yorumlar anonim olarak görünüyor
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A181C)
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    repeat(5) { index ->
                        val starIcon = if (index < review.rating) {
                            R.drawable.ic_star_filled
                        } else {
                            R.drawable.ic_star_outline
                        }
                        Icon(
                            painter = painterResource(id = starIcon),
                            contentDescription = null,
                            tint = Color(0xFF5BC658),
                            modifier = Modifier.size(19.76.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = review.content,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF707070)
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))
            }
        }
        Divider(color = Color(0xFFBFBFBF), thickness = 1.dp, modifier = Modifier.padding(horizontal = 16.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    course: Course,
    onNavigateBack: () -> Unit,
    onRateCourseClick: () -> Unit



) {
    var selectedTab by remember { mutableStateOf("Courses") }
    Scaffold(
        containerColor = Color(0xFFF3F3F3),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Reviews",
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 25.sp,
                            color = Color(0xFF5F5464)
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
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

            // Course Card
            Card(
                modifier = Modifier
                    .width(380.dp)
                    .height(169.dp)
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Course Code Box
                    Box(
                        modifier = Modifier
                            .width(116.dp)
                            .height(46.dp)
                            .background(Color(0xFF85C0FF), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = course.code,
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Course Name and Instructor
                    Text(
                        text = course.name,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF342E37)
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = course.instructor,
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF707070)
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rate Course Button
                    TextButton(
                        onClick = onRateCourseClick,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(
                            text = "Rate Course",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF3C9BFF)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reviews List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(course.reviews) { review ->
                    CourseReviewItem(review = review)
                }
            }
        }
    }
}


@Composable
fun CourseReviewScreen(onCourseClick: (Int) -> Unit) {
    val courses = listOf(dummyCourse1, dummyCourse2)
    LazyColumn {
        items(courses) { course ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onCourseClick(course.id) }
            ) {
                Box(
                    modifier = Modifier
                        .width(116.dp)
                        .height(46.dp)
                        .background(Color(0xFF85C0FF), RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = course.code,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = course.name,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rating: ${course.rating}",
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                    )
                }
            }
        }
    }
}

val dummyCourse1 = Course(
    id = 1,
    code = "VCD 471",
    name = "Interactive Design Studio",
    rating = 5f,
    instructor = "Merve Çaşkurlu",
    reviews = listOf(
        Review(1, "Anonim", "Ders baya iyi.", 5, courseId = 1),
        Review(2, "Anonim", "Ders çok zor hiç sevmedim.", 1, courseId = 1),
        Review(3, "Anonim", "Tasarım yapmayı sevenler bu dersi seçebilir.", 5, courseId = 1)
    )
)

val dummyCourse2 = Course(
    id = 2,
    code = "VCD 592",
    name = "Internship",
    rating = 3f,
    instructor = "Murat Yılmaz",
    reviews = listOf(
        Review(4, "Anonim", "Ders çok faydalı.", 4, courseId = 2),
        Review(5, "Anonim", "Zorunlu olması çok kötü.", 2, courseId = 2),
        Review(6, "Anonim", "Sevmedim.", 3, courseId = 2)
    )
)

@Composable
fun TestCourseDetailsScreen() {
    CourseDetailsScreen(
        course = dummyCourse1, // veya dummyCourse2
        onNavigateBack = { /* Geri gitme işlevi */ },
        onRateCourseClick = { /* Rate course işlevi */ }
    )
}