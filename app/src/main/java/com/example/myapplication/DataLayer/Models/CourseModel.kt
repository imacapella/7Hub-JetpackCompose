package com.example.myapplication.DataLayer.Models

data class CourseModel(
    val Identifier: String = "",
    val courseName: String = "",
    val courseDesc: String? = null,
    val instructor: String? = null,
    val courseCredit: Int? = null,
    val courseRating: Int = 0,
    val semester: String? = null,
    val prerequisites: List<String> = emptyList(),
    val syllabus: String? = null,
    val announcements: List<String> = emptyList(),
    val instructorId: String? = null,
    val instructorRef: String? = null,
    val instructorImageUrl: String? = null
)
