

//            // Course Card
// // Course Card içerisindeki düzenlemeler
//            // Course Details Card içerisine öğretmen ismini dersin adı altına taşıdık.
//            Card(
//                modifier = Modifier
//                    .width(380.dp)
//                    .height(169.dp)
//                    .padding(2.dp)  // Dış boşluk
//                    .align(Alignment.CenterHorizontally),
//                shape = RoundedCornerShape(10.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(8.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        // Course Code Box
//                        Box(
//                            modifier = Modifier
//                                .width(116.dp)
//                                .height(46.dp)
//                                .background(Color(0xFF85C0FF), RoundedCornerShape(8.dp)),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = course.code,
//                                style = TextStyle(
//                                    fontFamily = OpenSansSemiBold,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 22.sp,
//                                    color = Color.White
//                                )
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.width(16.dp))
//
//                        // Course Name
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = course.name,
//                                style = TextStyle(
//                                    fontFamily = OpenSansSemiBold,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 16.sp,
//                                    color = Color(0xFF342E37)
//                                ),
//                                maxLines = 2, // İki satırda yazması için
//                                overflow = TextOverflow.Ellipsis
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Stars under the blue box
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Start // Sol hizalama
//                    ) {
//                        Spacer(modifier = Modifier.width(16.dp)) // Mavi kutunun hizasında olması için
//                        repeat(5) { index ->
//                            val starIcon = when {
//                                index < course.rating.toInt() -> R.drawable.ic_star_filled
//                                index == course.rating.toInt() && course.rating % 1f != 0f -> R.drawable.ic_star_half
//                                else -> R.drawable.ic_star_outline
//                            }
//                            Icon(
//                                painter = painterResource(id = starIcon),
//                                contentDescription = null,
//                                tint = Color(0xFF5BC658),
//                                modifier = Modifier.size(19.76.dp)
//                            )
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//
//                    // Öğretmen ismini kartın ortasında ve üste yakın konumlandırıyoruz
//                        Text(
//                            text = course.instructor,
//                            style = TextStyle(
//                                fontFamily = OpenSansSemiBold,
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                color = Color(0xFF5BC658)
//                            ),
//                            modifier = Modifier
//                                .align(Alignment.CenterHorizontally) // Ortada hizalama
//                                .offset(y = (-40).dp) // Yukarıya taşıma
//                        )
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Rate Course Button
//                    Button(
//                        onClick = onRateCourseClick,
//                        modifier = Modifier
//                            .align(Alignment.End)  // Butonu sağa hizalıyoruz
//                            .padding(2.dp),
//                        shape = RoundedCornerShape(20),  // Butonun köşeleri yuvarlatılmış
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Color.Transparent // Arka planı şeffaf yapıyoruz
//                        )
//                    ) {
//                        Row {
//                            Text(
//                                text = "Rate Course",  // Buton metni
//                                style = TextStyle(
//                                    fontFamily = OpenSansSemiBold,
//                                    fontWeight = FontWeight.SemiBold,
//                                    fontSize = 13.sp,
//                                    color = Color(0xFF5BC658)  // Yeşil renkli metin
//                                )
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))  // Metin ve ikon arasına boşluk ekliyoruz
//                            Icon(
//                                painter = painterResource(id = R.drawable.ic_arrow_right),  // Ok simgesi
//                                contentDescription = "Arrow Right",
//                                tint = Color(0xFF5BC658),  // İkonun yeşil rengi
//                                modifier = Modifier.size(18.dp)  // İkon boyutu
//                            )
//                        }
//                    }
//                }
//            }
//
//
//
//
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            // Reviews List
//            LazyColumn(modifier = Modifier.fillMaxSize()) {
//                items(course.reviews) { review ->
//                    CourseReviewItem(review = review)
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun CourseReviewScreen(onCourseClick: (Int) -> Unit) {
//    val courses = listOf(dummyCourse1, dummyCourse2)
//    LazyColumn {
//        items(courses) { course ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//                    .clickable { onCourseClick(course.id) }
//            ) {
//                Box(
//                    modifier = Modifier
//                        .width(116.dp)
//                        .height(46.dp)
//                        .background(Color(0xFF85C0FF), RoundedCornerShape(8.dp)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = course.code,
//                        style = TextStyle(
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 22.sp,
//                            color = Color.White
//                        )
//                    )
//                }
//                Spacer(modifier = Modifier.width(16.dp))
//                Column {
//                    Text(
//                        text = course.name,
//                        style = TextStyle(
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 16.sp
//                        )
//                    )
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = "Rating: ${course.rating}",
//                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
//                    )
//                }
//            }
//        }
//    }
//}
//
//val dummyCourse1 = Course(
//    id = 1,
//    code = "VCD 471",
//    name = "Interactive Design Studio",
//    rating = 5f,
//    totalRatings = 5,
//    instructor = "Merve Çaşkurlu",
//    reviews = listOf(
//        Review(1, "Anonim", "Ders baya iyi.", 5, courseId = 1),
//        Review(2, "Anonim", "Ders çok zor hiç sevmedim.", 1, courseId = 1),
//        Review(3, "Anonim", "Tasarım yapmayı sevenler bu dersi seçebilir.", 5, courseId = 1)
//    )
//)
//
//val dummyCourse2 = Course(
//    id = 2,
//    code = "VCD 592",
//    name = "Internship",
//    rating = 2.8f,
//    totalRatings = 3,
//    instructor = "Murat Yılmaz",
//    reviews = listOf(
//        Review(4, "Anonim", "Ders çok faydalı.", 4, courseId = 2),
//        Review(5, "Anonim", "Zorunlu olması çok kötü.", 2, courseId = 2),
//        Review(6, "Anonim", "Sevmedim.", 3, courseId = 2)
//    )
//)
//
//@Composable
//fun TestCourseDetailsScreen() {
//    CourseDetailsScreen(
//        course = dummyCourse1, // veya dummyCourse2
//        onNavigateBack = { /* Geri gitme işlevi */ },
//        onRateCourseClick = { /* Rate course işlevi */ }
//    )
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewCourseReviewItem() {
//    val dummyReview = Review(1, "Anonim", "Ders baya iyi.", 5, courseId = 1)
//    CourseReviewItem(review = dummyReview)
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewCourseDetailsScreen() {
//    CourseDetailsScreen(
//        course = dummyCourse1,
//        onNavigateBack = { /* Geri gitme işlevi */ },
//        onRateCourseClick = { /* Rate course işlevi */ }
//    )
//}
//
//@Composable
//@Preview(showBackground = true)
//fun PreviewCourseReviewScreen() {
//    CourseReviewScreen(onCourseClick = { courseId -> /* Course click işlevi */ })
//}
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.ui.text.style.TextOverflow




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
            Image(
                painter = painterResource(id = R.drawable.profilepictureanonim),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {

                Text(
                    text =  "Anonim", // Her zaman "Anonim"
                    style = TextStyle(fontFamily = OpenSansSemiBold,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1A181C) ),


                    )

                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    repeat(5) { index ->
                        val starIcon = when {
                            index < review.rating -> R.drawable.ic_star_filled
                            index == review.rating.toInt() && review.rating % 1f != 0f -> R.drawable.ic_star_half
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
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = review.content,
                    style = TextStyle(
                        fontFamily = OpenSansSemiBold,  // Yorumlar için OpenSansSemiBold fontu
                        fontWeight = FontWeight.SemiBold,  // Yorumlar için yarı kalın yazı
                        fontSize = 15.sp,  // Yorum font boyutunu belirliyoruz
                        color = Color(0xFF707070)  // Yorumların rengini değiştirebiliriz
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CustomShadowButton(
                    text = "Teachers",
                    selected = selectedTab == "Teachers",
                    onClick = { selectedTab = "Teachers" }
                )
                Spacer(modifier = Modifier.width(16.dp))
                CustomShadowButton(
                    text = "Courses",
                    selected = selectedTab == "Courses",
                    onClick = { selectedTab = "Courses" }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Course Card
// Course Card içerisindeki düzenlemeler
            // Course Details Card içerisine öğretmen ismini dersin adı altına taşıdık.
            Card(
                modifier = Modifier
                    .width(380.dp)
                    .height(169.dp)
                    .padding(2.dp)  // Dış boşluk
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
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
                                    fontFamily = OpenSansSemiBold,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 22.sp,
                                    color = Color.White
                                )
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Course Name
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = course.name,
                                style = TextStyle(
                                    fontFamily = OpenSansSemiBold,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF342E37)
                                ),
                                maxLines = 2, // İki satırda yazması için
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Stars under the blue box
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start // Sol hizalama
                    ) {
                        Spacer(modifier = Modifier.width(16.dp)) // Mavi kutunun hizasında olması için
                        repeat(5) { index ->
                            val starIcon = when {
                                index < course.rating.toInt() -> R.drawable.ic_star_filled
                                index == course.rating.toInt() && course.rating % 1f != 0f -> R.drawable.ic_star_half
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

                    Spacer(modifier = Modifier.height(8.dp))


                    // Öğretmen ismini kartın ortasında ve üste yakın konumlandırıyoruz
                    Text(
                        text = course.instructor,
                        style = TextStyle(
                            fontFamily = OpenSansSemiBold,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = Color(0xFF5BC658)
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally) // Ortada hizalama
                            .offset(y = (-40).dp) // Yukarıya taşıma
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Rate Course Button
                    Button(
                        onClick = onRateCourseClick,
                        modifier = Modifier
                            .align(Alignment.End)  // Butonu sağa hizalıyoruz
                            .padding(2.dp),
                        shape = RoundedCornerShape(20),  // Butonun köşeleri yuvarlatılmış
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent // Arka planı şeffaf yapıyoruz
                        )
                    ) {
                        Row {
                            Text(
                                text = "Rate Course",  // Buton metni
                                style = TextStyle(
                                    fontFamily = OpenSansSemiBold,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF5BC658)  // Yeşil renkli metin
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))  // Metin ve ikon arasına boşluk ekliyoruz
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),  // Ok simgesi
                                contentDescription = "Arrow Right",
                                tint = Color(0xFF5BC658),  // İkonun yeşil rengi
                                modifier = Modifier.size(18.dp)  // İkon boyutu
                            )
                        }
                    }
                }
            }







            Spacer(modifier = Modifier.height(10.dp))

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
    rating = 2.8f,
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

@Composable
@Preview(showBackground = true)
fun PreviewCourseReviewItem() {
    val dummyReview = Review(1, "Anonim", "Ders baya iyi.", 5, courseId = 1)
    CourseReviewItem(review = dummyReview)
}

@Composable
@Preview(showBackground = true)
fun PreviewCourseDetailsScreen() {
    CourseDetailsScreen(
        course = dummyCourse1,
        onNavigateBack = { /* Geri gitme işlevi */ },
        onRateCourseClick = { /* Rate course işlevi */ }
    )
}

@Composable
@Preview(showBackground = true)
fun PreviewCourseReviewScreen() {
    CourseReviewScreen(onCourseClick = { courseId -> /* Course click işlevi */ })
}