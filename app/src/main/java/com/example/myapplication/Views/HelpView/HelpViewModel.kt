package com.example.myapplication.Views.HelpView

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HelpViewModel : ViewModel() {
    private val _helpText = MutableStateFlow("")
    val helpText: StateFlow<String> = _helpText.asStateFlow()

    fun updateHelpText(text: String) {
        _helpText.value = text
    }

    fun submitHelp() {
        // Firebase veya başka bir backend servisine gönderme işlemi
    }
} 