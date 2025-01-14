package com.example.myapplication.Views.ClubsView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.DataLayer.Models.ClubModel
import com.example.myapplication.DataLayer.Models.ClubTab
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await



class ClubsViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _selectedTab = MutableStateFlow(ClubTab.MY_CLUBS)
    val selectedTab: StateFlow<ClubTab> = _selectedTab.asStateFlow()

    private val _clubs = MutableStateFlow<List<ClubModel>>(emptyList())
    val clubs: StateFlow<List<ClubModel>> = _clubs.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadClubs()
    }

    fun onTabSelected(tab: ClubTab) {
        _selectedTab.value = tab
        loadClubs()
    }

    private fun loadClubs() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val currentUser = auth.currentUser
                
                when (_selectedTab.value) {
                    ClubTab.MY_CLUBS -> {
                        if (currentUser != null) {
                            // Kullanıcının üye olduğu kulüpleri getir
                            val clubsQuery = db.collection("clubs")
                                .whereArrayContains("members", currentUser.uid)
                                .get()
                                .await()

                            val myClubs = clubsQuery.documents.map { doc ->
                                ClubModel(
                                    clubId = doc.id,
                                    clubName = doc.getString("name") ?: "",
                                    clubIcon = doc.getString("icon") ?: "default",
                                    description = doc.getString("description") ?: "",
                                    memberCount = (doc.get("members") as? List<*>)?.size ?: 0,
                                    members = (doc.get("members") as? List<String>) ?: emptyList(),
                                    chatId = doc.id  // Club ID'si aynı zamanda chat ID'si
                                )
                            }
                            _clubs.value = myClubs
                        }
                    }
                    ClubTab.ALL_CLUBS -> {
                        // Tüm kulüpleri getir
                        val clubsQuery = db.collection("clubs")
                            .get()
                            .await()

                        val allClubs = clubsQuery.documents.map { doc ->
                            ClubModel(
                                clubId = doc.id,
                                clubName = doc.getString("name") ?: "",
                                clubIcon = doc.getString("icon") ?: "default",
                                description = doc.getString("description") ?: "",
                                memberCount = (doc.get("members") as? List<*>)?.size ?: 0,
                                members = (doc.get("members") as? List<String>) ?: emptyList()
                            )
                        }
                        _clubs.value = allClubs
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Kulüpler yüklenirken bir hata oluştu"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 