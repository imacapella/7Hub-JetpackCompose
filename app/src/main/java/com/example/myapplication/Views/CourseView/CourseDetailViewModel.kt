                                                                                                                                                        package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.DataLayer.Models.CourseModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class CourseDetailUiState(
    val isLoading: Boolean = true,
    val identifier: String = "",
    val courseName: String = "",
    val courseDesc: String = "",
    val instructor: String = "",
    val courseCredit: Int = 0,
    val courseRating: Int = 0,
    val semester: String = "",
    val prerequisites: List<String> = emptyList(),
    val syllabus: String = "",
    val announcements: List<String> = emptyList(),
    val instructorId: String = "",
    val instructorImageUrl: String = "",
    val userRole: String = "student"
)

class CourseDetailViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(CourseDetailUiState())
    val uiState: StateFlow<CourseDetailUiState> = _uiState.asStateFlow()

    fun loadCourseDetails(courseId: String) {
        viewModelScope.launch {
            try {
                Log.d("CourseDetailViewModel", "Loading course details for ID: $courseId")
                _uiState.value = _uiState.value.copy(isLoading = true)

                val document = firestore.collection("courses")
                    .document(courseId)
                    .get()
                    .await()

                if (document != null && document.exists()) {
                    Log.d("CourseDetailViewModel", "Course document found: ${document.data}")
                    try {
                        val instructorId = document.getString("instructorId") ?: ""
                        
                        val newState = _uiState.value.copy(
                            isLoading = false,
                            identifier = document.id,
                            courseName = document.getString("name") ?: "",
                            courseDesc = document.getString("description") ?: "",
                            instructor = document.getString("instructor") ?: "",
                            courseCredit = document.getLong("courseCredit")?.toInt() ?: 0,
                            courseRating = document.getLong("courseRating")?.toInt() ?: 0,
                            semester = document.getString("semester") ?: "",
                            prerequisites = document.get("prerequisites") as? List<String> ?: emptyList(),
                            syllabus = document.getString("syllabus") ?: "",
                            announcements = document.get("announcements") as? List<String> ?: emptyList(),
                            instructorId = instructorId
                        )
                        _uiState.value = newState
                        
                        // Instructor detaylarını yükle
                        if (instructorId.isNotEmpty()) {
                            loadInstructorDetails(instructorId)
                        }
                        
                        Log.d("CourseDetailViewModel", "Updated UI state: $newState")
                    } catch (e: Exception) {
                        Log.e("CourseDetailViewModel", "Error parsing course details", e)
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                } else {
                    Log.e("CourseDetailViewModel", "Course document not found for ID: $courseId")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            } catch (e: Exception) {
                Log.e("CourseDetailViewModel", "Error loading course details", e)
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
    private fun loadInstructorDetails(instructorId: String) {
        firestore.collection("instructors")
            .document(instructorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val photoUrl = document.getString("imageUrl") ?: ""
                    Log.d("CourseDetailViewModel", "Instructor image URL: $photoUrl")
                    
                    _uiState.value = _uiState.value.copy(
                        instructorImageUrl = photoUrl
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("CourseDetailViewModel", "Error loading instructor details", e)
            }
    }
}