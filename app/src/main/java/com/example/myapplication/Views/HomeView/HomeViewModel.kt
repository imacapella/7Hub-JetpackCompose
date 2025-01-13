package com.example.myapplication.Views.HomeView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.DataLayer.Models.CourseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _userName = MutableStateFlow<String?>(null)
    val userName = _userName.asStateFlow()
    
    private val _studentId = MutableStateFlow<String?>(null)
    val studentId = _studentId.asStateFlow()

    private val _courses = MutableStateFlow<List<CourseModel>>(emptyList())
    val courses = _courses.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val displayName = document.getString("displayName") ?: ""
                    _userName.value = displayName
                    _studentId.value = document.getString("studentId") ?: ""
                    
                    // Get user's courses
                    val userCourses = document.get("courses") as? List<String> ?: emptyList()
                    Log.d("HomeViewModel", "User courses: $userCourses")
                    loadUserCourses(userCourses)
                }
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error loading user data", e)
            }
    }

    private fun loadUserCourses(courseCodes: List<String>) {
        if (courseCodes.isEmpty()) {
            _courses.value = emptyList()
            Log.d("HomeViewModel", "No courses found for user")
            return
        }

        Log.d("HomeViewModel", "Loading courses: $courseCodes")

        firestore.collection("courses")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("HomeViewModel", "Total courses found: ${documents.size()}")
                val coursesList = documents.mapNotNull { doc ->
                    val courseId = doc.id
                    if (courseId in courseCodes) {
                        try {
                            CourseModel(
                                Identifier = courseId,
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
                                Log.d("HomeViewModel", "Successfully loaded course: ${it.courseName} with ID: ${it.Identifier}")
                            }
                        } catch (e: Exception) {
                            Log.e("HomeViewModel", "Error parsing course: $courseId", e)
                            Log.e("HomeViewModel", "Document data: ${doc.data}")
                            null
                        }
                    } else null
                }
                _courses.value = coursesList
                Log.d("HomeViewModel", "Final courses loaded: ${coursesList.size}")
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error loading courses", e)
                _courses.value = emptyList()
            }
    }
}