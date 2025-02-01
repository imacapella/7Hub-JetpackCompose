package com.example.myapplication.DataLayer.Models

//import com.example.myapplication.Views.ReviewScreen.Course
//import com.example.myapplication.Views.ReviewScreen.Review
//import com.example.myapplication.Views.ReviewScreen.Teacher
//import com.example.myapplication.R
//import com.google.firebase.database.*

//class DataRepository {

//    private val database = FirebaseDatabase.getInstance()

//    fun fetchTeachers(callback: (List<Teacher>) -> Unit) {
//        val teachersRef = database.getReference("teachers")

//        teachersRef.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val predefinedTeachers = listOf(
//                    Teacher(
//                        id = 1,
//                        name = "Merve Çaşkurlu",
//                        totalRatings = 5,
//                        photo = R.drawable.teacher_1,
//                        reviews = listOf(
//                            Review(id = 1, author = "anonim", content = "Hoca baya iyi!", totalRatings = 5, teacherId = 1),
//                            Review(id = 2, author = "anonim", content = "Hocayı sevmedim...", totalRatings = 5, teacherId = 1),
//                            Review(id = 3, author = "anonim", content = "Hocayı çok sevdim.", totalRatings = 5, teacherId = 1)
//                        ),
//                        rating = 3.5f
//                    ),
//                    Teacher(
//                        id = 2,
//                        name = "Murat Yılmaz",
//                        totalRatings = 3,
//                        photo = R.drawable.teacher_2,
//                        reviews = listOf(
//                            Review(id = 1, author = "anonim", content = "Anlatımı çok kötü.", totalRatings = 3, teacherId = 2),
//                            Review(id = 2, author = "anonim", content = "Güzel anlatıyor.", totalRatings = 4, teacherId = 2),
//                            Review(id = 3, author = "anonim", content = "Sevmedim..", totalRatings = 2, teacherId = 2)
//                        ),
//                        rating = 3.0f
//                    )
//                )

//                val updatedTeachers = predefinedTeachers + snapshot.children.mapNotNull { child ->
//                    val id = child.key?.toIntOrNull() ?: return@mapNotNull null
//                    val totalRatings = (child.child("totalRatings").value as? Long)?.toInt() ?: return@mapNotNull null

//                    Teacher(
//                        id = id,
//                        name = child.child("name").value as? String ?: "Unknown",
//                        totalRatings = totalRatings,
//                        photo =
//                        reviews = emptyList(),
//                        rating = totalRatings.toFloat()
//                    )
//                }

//                callback(updatedTeachers)
//            }

//            override fun onCancelled(error: DatabaseError) {
//                callback(emptyList())
//            }
//        })
//    }

//    fun fetchCourses(callback: (List<Course>) -> Unit) {
//        val courses = listOf(
//            Course(
//                id = "VCD471",
//                name = "Interactive Design Studio",
//                instructor = "Merve Çaşkurlu",
//                totalRatings = 100,
//                reviews = listOf(
//                    Review(id = 1, author = "anonim", content = "Ders baya iyi!", totalRatings = 5, courseId = 1),
//                    Review(id = 2, author = "anonim", content = "Ders çok zor.....", totalRatings = 2, courseId = 1),
//                    Review(id = 3, author = "anonim", content = "Tasarım dersini sevenler mutlaka alsın.", totalRatings = 5, courseId = 1)
//                )
//            ),
//            Course(
//                id = "VCD592",
//                name = "Internship",
//                instructor = "Murat Yılmaz",
//                totalRatings = 50,
//                reviews = listOf(
//                    Review(id = 1, author = "anonim", content = "Çok zor...", totalRatings = 2, courseId = 2),
//                    Review(id = 2, author = "anonim", content = "Zorunluluğu kalkmalı...", totalRatings = 3, courseId = 2),
//                    Review(id = 3, author = "anonim", content = "Çok faydalı bi ders.", totalRatings = 5, courseId = 2)
//                )
//            )
//        )
//        callback(courses)
//    }
//}
