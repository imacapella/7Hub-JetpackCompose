//package com.example.myapplication.Views.ReviewScreen

//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavController
//import com.example.myapplication.R

//@Composable
//fun ReviewCoursesScreen(
//    navController: NavController, // NavController'ı parametre olarak alıyoruz
//    courses: List<Course> = listOf(
//        Course(id = 1, code = "VCD 471", name = "Interactive Design Studio", instructor = "Merve Çaşkurlu", rating = 5.0f),
//        Course(id = 2, code = "VCD 592", name = "Internship", instructor = "Murat Yılmaz", rating = 2.8f)
//    ),
//    onNavigateBack: () -> Unit = {}
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF3F3F3))
//    ) {
//        // Üstteki başlık ve geri dönme düğmesi
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp, vertical = 16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onNavigateBack) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_left),
//                    contentDescription = "Back",
//                    tint = Color(0xFF3C9BFF),
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(
//                text = "Reviews",
//                style = TextStyle(
//                    fontWeight = FontWeight.SemiBold,
//                    fontSize = 25.sp,
//                    color = Color(0xFF5F5464)
//                ),
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Kursların listesi
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 16.dp)
//        ) {
//            items(courses) { course ->
//                CourseItem(
//                    course = course,
//                    onCourseClick = { navController.navigate("course_details/${course.id}") }  // Burada doğru şekilde lambda işlevi geçiliyor
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun CourseItem(
//    course: Course,
//    onCourseClick: (Course) -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .size(width = 360.dp, height = 142.dp),
//        shape = RoundedCornerShape(10.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxSize(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Column(
//                    modifier = Modifier
//                        .weight(1f)
//                        .fillMaxHeight()
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .width(116.dp)
//                            .height(46.dp)
//                            .background(Color(0xFF85C0FF), RoundedCornerShape(8.dp)),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = course.code,
//                            style = TextStyle(
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 22.sp,
//                                color = Color.White
//                            )
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    Row(
//                        modifier = Modifier
//                            .width(112.63.dp)
//                            .height(19.76.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        repeat(5) { index ->
//                            val starIcon = when {
//                                index < course.rating.toInt() -> R.drawable.ic_star_filled
//                                index < course.rating.toInt() + 1 && course.rating % 1 != 0f -> R.drawable.ic_star_half
//                                else -> R.drawable.ic_star_outline
//                            }
//
//                            Icon(
//                                painter = painterResource(id = starIcon),
//                                contentDescription = null,
//                                tint = Color(0xFF5BC658),
//                                modifier = Modifier.size(19.76.dp)
//                            )
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                Column(
//                    modifier = Modifier
//                        .weight(2f)
//                        .padding(start = 40.dp)
//                        .fillMaxWidth(),
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Text(
//                        text = if (course.name == "Interactive Design Studio") {
//                            "Interactive\nDesign Studio"
//                        } else {
//                            course.name
//                        },
//                        style = TextStyle(
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 16.sp,
//                            color = Color(0xFF342E37)
//                        ),
//                        maxLines = 2,
//                        softWrap = true
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = course.instructor,
//                        style = TextStyle(
//                            fontWeight = FontWeight.Normal,
//                            fontSize = 14.sp,
//                            color = Color(0xFF379634)
//                        )
//                    )
//                }
//            }
//
//            IconButton(
//                onClick = { onCourseClick(course) },  // Buradaki işlevi tetikleme
//                modifier = Modifier
//                    .size(40.dp)
//                    .align(Alignment.BottomEnd) // Box içinde sağ alt köşeye hizalama
//                    .padding(bottom = 4.dp)
//            ) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_right),
//                    contentDescription = "Arrow",
//                    tint = Color(0xFF379634),
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//        }
//    }
//}
package com.example.myapplication.Views.ReviewScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.myapplication.R

@Composable
fun ReviewCoursesScreen(
    navController: NavController,
    onNavigateBack: () -> Unit = {}
) {
    // Dummy courses listesi
    val courses = listOf(
        Course(id = 1, code = "VCD 471", name = "Interactive Design Studio", instructor = "Merve Çaşkurlu", rating = 5.0f),
        Course(id = 2, code = "VCD 592", name = "Internship", instructor = "Murat Yılmaz", rating = 2.8f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {
        // Üstteki başlık ve geri dönme düğmesi
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_left),
                    contentDescription = "Back",
                    tint = Color(0xFF3C9BFF),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reviews",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    color = Color(0xFF5F5464)
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kursların listesi
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(courses) { course ->
                CourseItem(
                    course = course,
                    onCourseClick = {
                        // Tıklanan kursun detay sayfasına git
                        navController.navigate("coursesReview/${course.id}")
                    }
                )
            }
        }
    }
}


@Composable
fun CourseItem(
    course: Course,
    onCourseClick: (Course) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .size(width = 360.dp, height = 142.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
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

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .width(112.63.dp)
                            .height(19.76.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(5) { index ->
                            val starIcon = when {
                                index < course.rating.toInt() -> R.drawable.ic_star_filled
                                index < course.rating.toInt() + 1 && course.rating % 1 != 0f -> R.drawable.ic_star_half
                                else -> R.drawable.ic_star_outline
                            }

                            Icon(
                                painter = painterResource(id = starIcon),
                                contentDescription = null,
                                tint = Color(0xFF5BC658),
                                modifier = Modifier.size(19.76.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 40.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = if (course.name == "Interactive Design Studio") {
                            "Interactive\nDesign Studio"
                        } else {
                            course.name
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color(0xFF342E37)
                        ),
                        maxLines = 2,
                        softWrap = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = course.instructor,
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color(0xFF379634)
                        )
                    )
                }
            }

            IconButton(
                onClick = { onCourseClick(course) },
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Arrow",
                    tint = Color(0xFF379634),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
