                                                                                                                                                        package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class CourseDetailUiState(
    val courseCode: String = "",
    val courseTitle: String = "",
    val courseDescription: String? = null,
    val instructorName: String? = null,
    val instructorImageUrl: String? = null,
    val rating: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val credits: Int? = null,
    val semester: String? = null,
    val prerequisites: List<String> = emptyList(),
    val syllabus: String? = null,
    val announcements: List<String> = emptyList(),
    val userRole: String = "student"
)

class CourseDetailViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val _uiState = MutableStateFlow(CourseDetailUiState())
    val uiState: StateFlow<CourseDetailUiState> = _uiState.asStateFlow()

    fun loadCourseDetails(courseCode: String) {
        viewModelScope.launch {
            Log.d("CourseDetailViewModel", "Starting to load course details for code: $courseCode")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                Log.d("CourseDetailViewModel", "Fetching course from Firestore...")
                val courseTask = firestore.collection("courses")
                    .whereEqualTo("courseCode", courseCode)
                    .get()
                    .await()

                if (courseTask.isEmpty) {
                    Log.w("CourseDetailViewModel", "No course found with code: $courseCode")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Course not found"
                    )
                    return@launch
                }

                val document = courseTask.documents.first()
                Log.d("CourseDetailViewModel", "Course document found. ID: ${document.id}")
                
                // Log raw data
                Log.d("CourseDetailViewModel", "Raw course data: " +
                    "\nCode: ${document.getString("courseCode")}" +
                    "\nName: ${document.getString("courseName")}" +
                    "\nDesc: ${document.getString("courseDesc")}" +
                    "\nInstructor: ${document.getString("instructor")}" +
                    "\nCredits: ${document.getLong("courseCredit")}")
                
                // Kurs bilgilerini state'e kaydet
                val updatedState = CourseDetailUiState(
                    courseCode = document.getString("courseCode") ?: "",
                    courseTitle = document.getString("courseName") ?: "",
                    courseDescription = document.getString("courseDesc") 
                        ?: document.getString("description"),
                    instructorName = document.getString("instructor"),
                    instructorImageUrl = document.getString("instructorImageUrl"),
                    rating = document.getLong("courseRating")?.toInt() ?: 0,
                    credits = document.getLong("courseCredit")?.toInt(),
                    semester = document.getString("semester"),
                    prerequisites = (document.get("prerequisites") as? List<String>) ?: emptyList(),
                    syllabus = document.getString("syllabus"),
                    announcements = (document.get("announcements") as? List<String>) ?: emptyList(),
                    isLoading = false
                )

                Log.d("CourseDetailViewModel", "Updated state created: " +
                    "\nTitle: ${updatedState.courseTitle}" +
                    "\nDesc: ${updatedState.courseDescription}" +
                    "\nInstructor: ${updatedState.instructorName}" +
                    "\nCredits: ${updatedState.credits}")

                _uiState.value = updatedState
                Log.d("CourseDetailViewModel", "State updated with course details")

                // Kullanıcı rolünü kontrol et
                val currentUser = FirebaseAuth.getInstance().currentUser
                if (currentUser != null) {
                    Log.d("CourseDetailViewModel", "Current user found. ID: ${currentUser.uid}")
                    try {
                        val userDoc = firestore.collection("users")
                            .document(currentUser.uid)
                            .get()
                            .await()
                        
                        val userRole = userDoc.getString("role") ?: "student"
                        Log.d("CourseDetailViewModel", "User role loaded: $userRole")
                        _uiState.value = _uiState.value.copy(userRole = userRole)
                        Log.d("CourseDetailViewModel", "State updated with user role")
                    } catch (e: Exception) {
                        Log.e("CourseDetailViewModel", "Error loading user role", e)
                        _uiState.value = _uiState.value.copy(userRole = "student")
                    }
                } else {
                    Log.w("CourseDetailViewModel", "No current user found")
                }

                // Load instructor details if available
                val instructorId = document.getString("instructorId")
                if (instructorId != null) {
                    Log.d("CourseDetailViewModel", "Loading instructor details for ID: $instructorId")
                    try {
                        val instructorDoc = firestore.collection("users")
                            .document(instructorId)
                            .get()
                            .await()

                        if (instructorDoc.exists()) {
                            Log.d("CourseDetailViewModel", "Instructor document found: " +
                                "\nName: ${instructorDoc.getString("name")}" +
                                "\nPhoto: ${instructorDoc.getString("photoUrl")}")
                            
                            val previousState = _uiState.value
                            _uiState.value = _uiState.value.copy(
                                instructorName = instructorDoc.getString("name") ?: _uiState.value.instructorName,
                                instructorImageUrl = instructorDoc.getString("photoUrl") ?: _uiState.value.instructorImageUrl
                            )
                            Log.d("CourseDetailViewModel", "State updated with instructor details. " +
                                "Previous name: ${previousState.instructorName}, " +
                                "New name: ${_uiState.value.instructorName}")
                        } else {
                            Log.w("CourseDetailViewModel", "No instructor document found for ID: $instructorId")
                        }
                    } catch (e: Exception) {
                        Log.e("CourseDetailViewModel", "Error loading instructor details", e)
                    }
                } else {
                    Log.w("CourseDetailViewModel", "No instructorId found in course document")
                }
            } catch (e: Exception) {
                Log.e("CourseDetailViewModel", "Error loading course details", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error loading course details"
                )
            } finally {
                Log.d("CourseDetailViewModel", "Final state: " +
                    "\nTitle: ${_uiState.value.courseTitle}" +
                    "\nDesc: ${_uiState.value.courseDescription}" +
                    "\nInstructor: ${_uiState.value.instructorName}" +
                    "\nCredits: ${_uiState.value.credits}" +
                    "\nLoading: ${_uiState.value.isLoading}" +
                    "\nError: ${_uiState.value.error}")
            }
        }
    }

    private fun loadInstructorDetails(instructorId: String) {
        firestore.collection("users")
            .document(instructorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Mevcut state'i koru, sadece instructor bilgilerini güncelle
                    _uiState.value = _uiState.value.copy(
                        instructorName = document.getString("name") ?: _uiState.value.instructorName,
                        instructorImageUrl = document.getString("photoUrl") ?: _uiState.value.instructorImageUrl
                    )
                }
            }
            .addOnFailureListener { e ->
                Log.e("CourseDetailViewModel", "Error loading instructor details", e)
            }
    }
}