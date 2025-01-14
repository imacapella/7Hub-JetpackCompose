                                                                                                                                                        package com.example.myapplication.Views.CourseView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.DataLayer.Models.CourseModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue

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
    private val auth = FirebaseAuth.getInstance()
    private val currentUserId = auth.currentUser?.uid
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

    fun joinOrCreateCourseChat(courseCode: String, onSuccess: (String) -> Unit) {
        if (currentUserId == null) return

        // Önce bu koda sahip bir chat grubu var mı kontrol et
        firestore.collection("chats")
            .whereEqualTo("name", courseCode)
            .whereEqualTo("type", "GROUP")
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Chat grubu yok, yeni oluştur
                    createNewCourseChat(courseCode, onSuccess)
                } else {
                    // Chat grubu var, kullanıcıyı ekle
                    val chatId = documents.documents.first().id
                    addUserToChat(chatId, onSuccess)
                }
            }
            .addOnFailureListener { e ->
                Log.e("CourseDetailViewModel", "Error checking course chat", e)
            }
    }

    private fun createNewCourseChat(courseCode: String, onSuccess: (String) -> Unit) {
        val chatData = hashMapOf(
            "name" to courseCode,
            "type" to "GROUP",
            "createdAt" to com.google.firebase.Timestamp.now(),
            "lastMessage" to "",
            "lastMessageTimestamp" to com.google.firebase.Timestamp.now(),
            "participants" to listOf(currentUserId),
            "isClassGroup" to true
        )

        firestore.collection("chats")
            .add(chatData)
            .addOnSuccessListener { documentRef ->
                val chatId = documentRef.id
                
                // Kullanıcının userChats koleksiyonuna da ekle
                val userChatData = hashMapOf(
                    "lastMessageTimestamp" to com.google.firebase.Timestamp.now(),
                    "unreadCount" to 0
                )

                firestore.collection("userChats")
                    .document(currentUserId!!)
                    .collection("chats")
                    .document(chatId)
                    .set(userChatData)
                    .addOnSuccessListener {
                        onSuccess(chatId)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("CourseDetailViewModel", "Error creating course chat", e)
            }
    }

    private fun addUserToChat(chatId: String, onSuccess: (String) -> Unit) {
        // Önce participants listesini güncelle
        firestore.collection("chats").document(chatId)
            .update("participants", FieldValue.arrayUnion(currentUserId))
            .addOnSuccessListener {
                // Sonra userChats koleksiyonuna ekle
                val userChatData = hashMapOf(
                    "lastMessageTimestamp" to com.google.firebase.Timestamp.now(),
                    "unreadCount" to 0
                )

                firestore.collection("userChats")
                    .document(currentUserId!!)
                    .collection("chats")
                    .document(chatId)
                    .set(userChatData)
                    .addOnSuccessListener {
                        onSuccess(chatId)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("CourseDetailViewModel", "Error adding user to chat", e)
            }
    }
}