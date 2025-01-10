package com.example.myapplication.Views.ResetPassword

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ResetPasswordViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()
    
    private val _isEmailSent = MutableStateFlow(false)
    val isEmailSent = _isEmailSent.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _email.value = newEmail
    }

    fun sendResetEmail() {
        auth.sendPasswordResetEmail(_email.value)
            .addOnSuccessListener {
                _isEmailSent.value = true
                _errorMessage.value = null
            }
            .addOnFailureListener { e ->
                _errorMessage.value = e.message
            }
    }
}
