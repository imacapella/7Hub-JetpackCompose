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
    private val _courses = MutableStateFlow<List<CourseModel>>(emptyList())
    val courses: StateFlow<List<CourseModel>> = _courses.asStateFlow()

    private val _selectedTab = MutableStateFlow(CourseTab.MY_COURSES)
    val selectedTab: StateFlow<CourseTab> = _selectedTab.asStateFlow()

    private var allCoursesList: List<CourseModel> = emptyList()

    init {
        loadUserCourses()
    }

    private fun loadExistingCourses() {
        if (allCoursesList.isNotEmpty()) {
            _courses.value = allCoursesList
            return
        }

        firestore.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                val coursesList = documents.mapNotNull { doc ->
                    CourseModel(
                        courseId = doc.id,
                        courseCode = doc.getString("courseCode") ?: return@mapNotNull null,
                        courseName = doc.getString("courseName") ?: return@mapNotNull null,
                        description = doc.getString("description") ?: "",
                        instructor = doc.getString("instructor") ?: return@mapNotNull null
                    )
                }
                allCoursesList = coursesList
                _courses.value = coursesList
                Log.d("CoursesViewModel", "Loaded ${coursesList.size} courses")
            }
            .addOnFailureListener { e ->
                Log.e("CoursesViewModel", "Error loading all courses", e)
                _courses.value = emptyList()
            }
    }

    fun onTabSelected(tab: CourseTab) {
        if (_selectedTab.value == tab) return // Eğer aynı tab'a tıklandıysa işlem yapma
        
        _selectedTab.value = tab
        when (tab) {
            CourseTab.ALL_COURSES -> loadExistingCourses()
            CourseTab.MY_COURSES -> loadUserCourses()
        }
    }

    private fun loadUserCourses() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Log.d("CoursesViewModel", "No user logged in")
            _courses.value = emptyList()
            return
        }

        firestore.collection("users").document(currentUser.uid)
            .get()
            .addOnSuccessListener { userDoc ->
                val userCourses = userDoc.get("courses") as? List<String> ?: emptyList()
                Log.d("CoursesViewModel", "User courses: $userCourses")

                if (userCourses.isEmpty()) {
                    _courses.value = emptyList()
                    return@addOnSuccessListener
                }

                firestore.collection("courses")
                    .get()
                    .addOnSuccessListener { documents ->
                        val coursesList = documents.mapNotNull { doc ->
                            val courseCode = doc.getString("courseCode")
                            if (courseCode in userCourses) {
                                CourseModel(
                                    courseId = doc.id,
                                    courseCode = courseCode!!,
                                    courseName = doc.getString("courseName") ?: return@mapNotNull null,
                                    description = doc.getString("description") ?: "",
                                    instructor = doc.getString("instructor") ?: return@mapNotNull null
                                )
                            } else null
                        }
                        _courses.value = coursesList
                        Log.d("CoursesViewModel", "Loaded ${coursesList.size} user courses")
                    }
                    .addOnFailureListener { e ->
                        Log.e("CoursesViewModel", "Error loading courses", e)
                        _courses.value = emptyList()
                    }
            }
            .addOnFailureListener { e ->
                Log.e("CoursesViewModel", "Error loading user data", e)
                _courses.value = emptyList()
            }
    }
}

enum class CourseTab {
    MY_COURSES,
    ALL_COURSES
}

