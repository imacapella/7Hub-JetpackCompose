package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.myapplication.DataLayer.Models.CourseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CoursesViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _selectedTab = MutableStateFlow(CourseTab.MY_COURSES)
    val selectedTab: StateFlow<CourseTab> = _selectedTab.asStateFlow()

    private val _courses = MutableStateFlow<List<CourseModel>>(emptyList())
    val courses: StateFlow<List<CourseModel>> = _courses.asStateFlow()

    init {
        loadUserCourses()
    }

    private fun loadExistingCourses() {
        Log.d("CoursesViewModel", "Loading existing courses")
        firestore.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("CoursesViewModel", "Found ${documents.size()} courses")
                val coursesList = documents.mapNotNull { doc ->
                    try {
                        CourseModel(
                            Identifier = doc.id,
                            courseName = doc.getString("name") ?: return@mapNotNull null,
                            courseDesc = doc.getString("description"),
                            instructor = doc.getString("instructor"),
                            courseCredit = doc.getLong("courseCredit")?.toInt(),
                            courseRating = doc.getLong("courseRating")?.toInt() ?: 0,
                            semester = doc.getString("semester"),
                            prerequisites = doc.get("prerequisites") as? List<String> ?: emptyList(),
                            syllabus = doc.getString("syllabus"),
                            announcements = doc.get("announcements") as? List<String> ?: emptyList(),
                            instructorId = doc.getString("instructorId"),
                            instructorRef = doc.getDocumentReference("instructorRef")?.path,
                            instructorImageUrl = doc.getString("instructorImageUrl")
                        ).also {
                            Log.d("CoursesViewModel", "Successfully loaded course: ${it.courseName}")
                        }
                    } catch (e: Exception) {
                        Log.e("CoursesViewModel", "Error parsing course: ${doc.id}", e)
                        null
                    }
                }
                _courses.value = coursesList
                Log.d("CoursesViewModel", "Loaded ${coursesList.size} courses")
            }
            .addOnFailureListener { e ->
                Log.e("CoursesViewModel", "Error loading courses", e)
                _courses.value = emptyList()
            }
    }

    private fun loadUserCourses() {
        val userId = auth.currentUser?.uid ?: return
        Log.d("CoursesViewModel", "Loading user courses for: $userId")

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userCourses = document.get("courses") as? List<String> ?: emptyList()
                    Log.d("CoursesViewModel", "User courses: $userCourses")

                    firestore.collection("courses")
                        .get()
                        .addOnSuccessListener { documents ->
                            val coursesList = documents.mapNotNull { doc ->
                                if (doc.id in userCourses) {
                                    try {
                                        CourseModel(
                                            Identifier = doc.id,
                                            courseName = doc.getString("name") ?: return@mapNotNull null,
                                            courseDesc = doc.getString("description"),
                                            instructor = doc.getString("instructor"),
                                            courseCredit = doc.getLong("courseCredit")?.toInt(),
                                            courseRating = doc.getLong("courseRating")?.toInt() ?: 0,
                                            semester = doc.getString("semester"),
                                            prerequisites = doc.get("prerequisites") as? List<String> ?: emptyList(),
                                            syllabus = doc.getString("syllabus"),
                                            announcements = doc.get("announcements") as? List<String> ?: emptyList(),
                                            instructorId = doc.getString("instructorId"),
                                            instructorRef = doc.getDocumentReference("instructorRef")?.path,
                                            instructorImageUrl = doc.getString("instructorImageUrl")
                                        ).also {
                                            Log.d("CoursesViewModel", "Successfully loaded user course: ${it.courseName}")
                                        }
                                    } catch (e: Exception) {
                                        Log.e("CoursesViewModel", "Error parsing course: ${doc.id}", e)
                                        null
                                    }
                                } else null
                            }
                            _courses.value = coursesList
                            Log.d("CoursesViewModel", "Loaded ${coursesList.size} user courses")
                        }
                        .addOnFailureListener { e ->
                            Log.e("CoursesViewModel", "Error loading user courses", e)
                            _courses.value = emptyList()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("CoursesViewModel", "Error loading user document", e)
                _courses.value = emptyList()
            }
    }

    fun onTabSelected(tab: CourseTab) {
        _selectedTab.value = tab
        when (tab) {
            CourseTab.ALL_COURSES -> loadExistingCourses()
            CourseTab.MY_COURSES -> loadUserCourses()
        }
    }
}

enum class CourseTab {
    MY_COURSES,
    ALL_COURSES
}
