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

import androidx.compose.ui.text.style.TextAlign
import com.example.myapplication.ui.theme.CustomShadowButton


// Review Item Composable
@Composable
fun ReviewItem(review: Review) {
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

// Teacher Details Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailsScreen(
    teacher: Teacher,
    onNavigateBack: () -> Unit,
    onRateTeacherClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf("Teachers") }

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

            // Teacher Details Card
            Card(
                modifier = Modifier
                    .width(380.dp)  // Kartın genişliğini sabitliyoruz
                    .height(169.dp)  // Kartın yüksekliğini sabitliyoruz
                    .padding(2.dp)  // Dış boşluk
                    .align(Alignment.CenterHorizontally),  // Kartı ortalıyoruz
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)  // İç boşluk
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp), // Yüksekliği sabit tutuyoruz
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = teacher.photo),
                            contentDescription = "Teacher Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp) // Resmin boyutunu sabit tutuyoruz
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = teacher.name,
                                style = TextStyle(fontFamily = OpenSansSemiBold,fontWeight = FontWeight.SemiBold,

                                    fontSize = 20.sp,
                                    color = Color(0xFF342E37)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    val starIcon = when {
                                        index < teacher.rating.toInt() -> R.drawable.ic_star_filled
                                        index < teacher.rating.toInt() + 1 && teacher.rating % 1 != 0f -> R.drawable.ic_star_half
                                        else -> R.drawable.ic_star_outline
                                    }
                                    Icon(
                                        painter = painterResource(id = starIcon),
                                        contentDescription = null,
                                        tint = Color(0xFF5BC658),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Rate Teacher Butonunu düzelttik
                    TextButton(
                        onClick = onRateTeacherClick,
                        modifier = Modifier
                            .align(Alignment.End)  // Butonu sağa hizalıyoruz
                    ) {
                        Text(
                            text = "Rate Teacher",
                            color = Color(0xFF5BC658),
                            style = TextStyle(
                                fontFamily = OpenSansSemiBold,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Rate",
                            tint = Color(0xFF5BC658)

                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teacher.reviews) { review ->
                    ReviewItem(review = review)
                }
            }
        }
    }
}



// Review Screen
@Composable
fun ReviewScreen(onTeacherClick: (Int) -> Unit) {
    val teachers = listOf(dummyTeacher1, dummyTeacher2)
    LazyColumn {
        items(teachers) { teacher ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onTeacherClick(teacher.id) }
            ) {
                Image(
                    painter = painterResource(id = teacher.photo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = teacher.name,
                        style = TextStyle(fontFamily = OpenSansSemiBold,
                            fontWeight = FontWeight.SemiBold, fontSize = 16.sp )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rating: ${teacher.rating}",
                        style = TextStyle(fontSize = 14.sp, color = Color.Gray)
                    )
                }
            }
        }
    }
}

// Test Verileri
val dummyTeacher1 = Teacher(
    id = 1,
    name = "Merve Çaşkurlu",
    rating = 5f,
    photo = R.drawable.teacher_1,
    reviews = listOf(
        Review(1, "Anonim", "Hoca baya iyi.", 5, teacherId = 1),
        Review(2, "Anonim", "Hocayı hiç sevmedim...", 1, teacherId = 1),
        Review(3, "Anonim", "Hoca çok ilgili.", 5, teacherId = 1)
    )
)

val dummyTeacher2 = Teacher(
    id = 2,
    name = "Murat Yılmaz",
    rating = 2.8f,
    photo = R.drawable.teacher_2,
    reviews = listOf(
        Review(4, "Anonim", "Hocanın sınavları çok zor.", 2, teacherId = 2),
        Review(5, "Anonim", "Anlatımı çok iyi.", 4, teacherId = 2),
        Review(6, "Anonim", "Hocaya alışamadım.", 1, teacherId = 2)
    )
)



@Composable
fun TestTeacherDetailsScreen() {
    TeacherDetailsScreen(
        teacher = dummyTeacher1, // veya dummyTeacher2
        onNavigateBack = { /* Geri gitme işlevi */ },
        onRateTeacherClick = { /* Rate teacher işlevi */ }
    )
}