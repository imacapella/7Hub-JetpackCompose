package com.example.myapplication.Views.AccountView

import androidx.lifecycle.ViewModel
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

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return
        
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    _uiState.value = AccountUiState(
                        displayName = document.getString("displayName") ?: "",
                        email = document.getString("email") ?: "",
                        photoUrl = document.getString("photoUrl"),
                        studentId = document.getString("studentId") ?: ""
                    )
                }
            }
    }

    fun signOut() {
        auth.signOut()
    }
} 