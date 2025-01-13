package com.example.myapplication.Views.HelpView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.DataLayer.Models.HelpDataModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HelpViewModel : ViewModel() {
    private val _helpText = MutableStateFlow("")
    val helpText: StateFlow<String> = _helpText.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    fun updateHelpText(text: String) {
        _helpText.value = text
    }

    fun submitHelp() {
        viewModelScope.launch {
            try {
                val helpData = HelpDataModel.create(_helpText.value)
                
                // Firestore'a kaydet
                db.collection("helpRequest")
                    .add(helpData.toMap())
                    .await()

                // Başarılı submission sonrası text'i temizle
                _helpText.value = ""
                
            } catch (e: Exception) {
                // Hata durumunda işlem
                println("Help submission error: ${e.message}")
            }
        }
    }
} 