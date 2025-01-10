package com.example.myapplication.Views.GroupsView

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

data class Group(
    val id: String,
    val name: String,
    val iconUrl: String? = null
)

class GroupsViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUserId = Firebase.auth.currentUser?.uid

    init {
        loadGroups()
    }

    private fun loadGroups() {
        currentUserId?.let { userId ->
            firestore.collection("userChats")
                .document(userId)
                .collection("chats")
                .whereEqualTo("type", "GROUP")
                .get()
                .addOnSuccessListener { result ->
                    val groupIds = result.map { it.id }
                    if (groupIds.isNotEmpty()) {
                        firestore.collection("chats")
                            .whereIn("chatId", groupIds)
                            .get()
                            .addOnSuccessListener { chatResults ->
                                val loadedGroups = chatResults.map { doc ->
                                    Group(
                                        id = doc.id,
                                        name = doc.getString("name") ?: "Unnamed Group",
                                        iconUrl = doc.getString("iconUrl")
                                    )
                                }
                                _groups.value = loadedGroups
                            }
                    } else {
                        _groups.value = emptyList()
                    }
                }
                .addOnFailureListener {
                    println("Error loading groups: ${it.message}")
                }
        }
    }

    fun createNewGroup(name: String, participantIds: List<String>) {
        currentUserId?.let { userId ->
            val allParticipants = participantIds + userId
            val newGroupRef = firestore.collection("chats").document()
            val groupData = hashMapOf(
                "name" to name,
                "type" to "GROUP",
                "createdAt" to Timestamp.now(),
                "lastMessage" to "Grup oluşturuldu",
                "lastMessageTimestamp" to Timestamp.now(),
                "participants" to allParticipants
            )

            newGroupRef.set(groupData)
                .addOnSuccessListener {
                    allParticipants.forEach { participantId ->
                        val userChatData = hashMapOf(
                            "lastMessageTimestamp" to Timestamp.now(),
                            "unreadCount" to 0
                        )
                        firestore.collection("userChats")
                            .document(participantId)
                            .collection("chats")
                            .document(newGroupRef.id)
                            .set(userChatData)
                    }
                    loadGroups() // Yeni grup eklenince güncelle
                }
                .addOnFailureListener {
                    println("Error creating group: ${it.message}")
                }
        }
    }
}
