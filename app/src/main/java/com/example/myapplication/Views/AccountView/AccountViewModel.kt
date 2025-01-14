package com.example.myapplication.Views.AccountView

import androidx.lifecycle.ViewModel
import com.example.myapplication.Views.LoginView.AuthState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AccountUiState(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val studentId: String = ""
)

class AccountViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    fun signOut() {
        auth.signOut()
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val photoUrl = document.getString("photoUrl")
                    println("Debug - Photo URL: $photoUrl") // URL'yi kontrol etmek için
                    
                    _uiState.value = AccountUiState(
                        displayName = document.getString("displayName") ?: "",
                        email = document.getString("email") ?: "",
                        photoUrl = photoUrl,
                        studentId = document.getString("studentId") ?: ""
                    )
                } else {
                    println("Debug - Document does not exist")
                }
            }
            .addOnFailureListener { e ->
                println("Debug - Error loading user data: ${e.message}")
            }
    }
} 