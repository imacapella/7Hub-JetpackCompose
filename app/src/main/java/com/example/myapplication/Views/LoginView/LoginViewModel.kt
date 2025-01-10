package com.example.myapplication.Views.LoginView

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class LoginViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }


    fun checkAuthStatus(){
        if(auth.currentUser==null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.endsWith("@std.yeditepe.edu.tr")
    }

    private fun extractNameFromEmail(email: String): Pair<String, String>? {
        val prefix = email.substringBefore("@")
        val parts = prefix.split(".")
        return if (parts.size == 2) {
            Pair(parts[0].capitalize(), parts[1].capitalize())
        } else null
    }

    fun login(email : String,password : String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Only @std.yeditepe.edu.tr email addresses are allowed")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful) {
                    val nameInfo = extractNameFromEmail(email)
                    if (nameInfo != null) {
                        saveUserData(nameInfo.first, nameInfo.second, email)
                    } else {
                        _authState.value = AuthState.Authenticated
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error occurred")
                }
            }
    }

    private fun saveUserData(firstName: String, lastName: String, email: String) {
        val userId = auth.currentUser?.uid ?: return

        // Önce mevcut kullanıcı sayısını al ve ona göre numara ver
        firestore.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val userCount = documents.size()
                val formattedCount = String.format("%04d", userCount + 1) // 4 haneli sayı formatı
                
                val userData = hashMapOf(
                    "displayName" to "$firstName $lastName",
                    "email" to email,
                    "photoUrl" to "",
                    "studentId" to "2021$formattedCount" // Örnek: 20210001, 20210002, ...
                )

                firestore.collection("users")
                    .document(userId)
                    .set(userData, SetOptions.merge())
                    .addOnSuccessListener {
                        Log.d("LoginViewModel", "User data saved successfully: $userData")
                        _authState.value = AuthState.Authenticated
                    }
                    .addOnFailureListener { e ->
                        Log.e("LoginViewModel", "Error saving user data", e)
                        _authState.value = AuthState.Error(e.message ?: "Failed to save user data")
                    }
            }
    }

    fun signup(email : String,password : String){

        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }

        if (!isValidEmail(email)) {
            _authState.value = AuthState.Error("Only @std.yeditepe.edu.tr email addresses are allowed")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task->
                if (task.isSuccessful){
                    val nameInfo = extractNameFromEmail(email)
                    if (nameInfo != null) {
                        saveUserData(nameInfo.first, nameInfo.second, email)
                    } else {
                        _authState.value = AuthState.Authenticated
                    }
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }


}


sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}