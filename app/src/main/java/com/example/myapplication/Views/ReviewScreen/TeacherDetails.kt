package com.example.myapplication.Views.ReviewScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

// Yorum Elemanı
@Composable
fun ReviewItem(review: Review) {
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
        Column {
            Text(
                text = review.reviewerName,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = review.content)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Rating: ${review.rating} stars")
        }
    }
}

// Öğretmen Detayları Ekranı
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDetailsScreen(
    teacher: Teacher,
    onNavigateBack: () -> Unit,
    onRateTeacherClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Teacher Details") },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_left),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(169.dp)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = teacher.photo),
                    contentDescription = "Teacher Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = teacher.name,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rating: ${teacher.rating}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    )
                }
            }

            Button(
                onClick = onRateTeacherClick,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                Text(text = "Rate Teacher")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = "Rate"
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teacher.reviews) { review ->
                    ReviewItem(review = review)
                }
            }
        }
    }
}

// Yorum Ekranı
@Composable
fun ReviewScreen(
    onTeacherClick: (Int) -> Unit
) {
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
                        style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
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

// Örnek Kullanım
@Composable
fun TestTeacherDetailsScreen() {
    TeacherDetailsScreen(
        teacher = dummyTeacher1, // veya dummyTeacher2
        onNavigateBack = { /* Geri gitme işlevi */ },
        onRateTeacherClick = { /* Rate teacher işlevi */ }
    )
}
