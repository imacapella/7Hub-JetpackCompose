//package com.example.myapplication.Views.ReviewScreen

//data class Review(
//    val id: Int? = null,
//    val author: String,
//    val content: String,
//    val totalRatings: Int,
//    val courseId: Int? = null,
//    val teacherId: Int? = null
//)

//data class Teacher(
//    val id: Int,
//    val name: String,
//    val totalRatings: Int,
//    val photo: Int, // Make sure this is non-nullable // Varsayılan resim için nullable hale getirildi
//    val reviews: List<Review> = emptyList(),
//    val rating: Float
//)

//data class Course(
//    val id: String,
//    val name: String,
//    val instructor: String,
//    val totalRatings: Int,
//    val reviews: List<Review> = emptyList(),
//    val code: String = ""
//)
package com.example.myapplication.Views.ReviewScreen

data class Review(
    val id: Int,
    val author: String,
    val content: String,
    val rating: Int,
    val courseId: Int? = null, // Ders için gerekli
    val teacherId: Int? = null // Öğretmen için gerekli
)


data class Teacher(
    val id: Int,
    val name: String,
    val rating: Float,
    val photo: Int, // Bu satır fotoğraf için
    val reviews: List<Review> = emptyList()  // Öğretmene ait yorumların listesi

)

data class Course(
    val id: Int,
    val code: String,         // Dersin kodu (örneğin: VCD 471)
    val name: String,         // Dersin adı
    val instructor: String,   // Dersi veren öğretmen
    val rating: Float,
    val reviews: List<Review> = emptyList()

// 1-5 arasında bir puan
)
