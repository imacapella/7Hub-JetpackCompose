package com.example.myapplication.Views.GroupsView

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Group(
    val id: String,
    val name: String,
    val iconUrl: String? = null
)

class GroupsViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    init {
        loadGroups()
    }

    private fun loadGroups() {
        // Simüle edilmiş grup verileri
        val groupsList = listOf(
            Group("1", "Sıkışınca Çalışırız"),
            Group("2", "Hızlı ve Bilgili"),
            Group("3", "Kafeinsiz Çalışmaz"),
            Group("4", "Finale Son 3 Gün"),
            Group("5", "HTR 301 Destek")
        )
        _groups.value = groupsList
    }

    fun createNewGroup(name: String) {
        val newGroup = Group(
            id = (groups.value.size + 1).toString(),
            name = name
        )
        _groups.value = _groups.value + newGroup
    }
}