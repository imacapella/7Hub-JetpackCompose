package com.example.myapplication.DataLayer.Models

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

data class HelpDataModel(
    val createdAt: Timestamp = Timestamp.now(),
    val displayName: String = "",
    val email: String = "",
    val request: String = "",
    val studentId: String = "",
) {
    // Firestore'a gönderilecek map formatı
    fun toMap(): Map<String, Any> {
        return mapOf(
            "createdAt" to createdAt,
            "displayName" to displayName,
            "email" to email,
            "request" to request,
            "studentId" to studentId
        )
    }

    companion object {
        fun create(helpText: String): HelpDataModel {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            return HelpDataModel(
                createdAt = Timestamp.now(),
                displayName = user?.displayName ?: "",
                email = user?.email ?: "",
                request = helpText,
                studentId = user?.uid ?: ""
            )
        }
    }
}