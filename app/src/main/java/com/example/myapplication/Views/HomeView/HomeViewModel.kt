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
                    _userName.value = displayName.split(" ").firstOrNull()
                    _studentId.value = document.getString("studentId") ?: ""
                    
                    // Kullanıcının sahip olduğu kurs ID'lerini al
                    val userCourseIds = document.get("courses") as? List<String> ?: emptyList()
                    loadUserCourses(userCourseIds)
                    
                    Log.d("HomeViewModel", "DisplayName: $displayName")
                    Log.d("HomeViewModel", "StudentId: ${_studentId.value}")
                    Log.d("HomeViewModel", "Course IDs: $userCourseIds")
                }
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error loading user data", e)
            }
    }

    private fun loadUserCourses(courseIds: List<String>) {
        if (courseIds.isEmpty()) {
            _courses.value = emptyList()
            return
        }

        firestore.collection("courses")
            .whereIn("courseId", courseIds)  // courseId'lere göre filtreleme
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
                _courses.value = coursesList
                Log.d("HomeViewModel", "Loaded courses: $coursesList")
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error loading courses", e)
            }
    }
}