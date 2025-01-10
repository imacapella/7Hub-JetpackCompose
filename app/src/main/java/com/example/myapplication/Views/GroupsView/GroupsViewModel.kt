package com.example.myapplication.Views.GroupsView

import android.util.Log
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
    val participants: List<String> = emptyList(),
    val iconUrl: String? = null
)

open class GroupsViewModel : ViewModel() {
    protected val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()
    private val firestore = FirebaseFirestore.getInstance()
    private val currentUserId = Firebase.auth.currentUser?.uid

    init {
        loadAllGroups()
    }

    protected open fun loadAllGroups() {
        firestore.collection("chats")
            .whereEqualTo("type", "GROUP")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("GroupsViewModel", "Error loading groups", error)
                    return@addSnapshotListener
                }

                val groupsList = snapshot?.documents?.map { doc ->
                    Group(
                        id = doc.id,
                        name = doc.getString("name") ?: "Unnamed Group",
                        participants = (doc.get("participants") as? List<String>) ?: emptyList()
                    )
                } ?: emptyList()

                _groups.value = groupsList
            }
    }

    open fun joinGroup(groupId: String, onSuccess: () -> Unit) {
        currentUserId?.let { userId ->
            val groupRef = firestore.collection("chats").document(groupId)
            
            firestore.runTransaction { transaction ->
                val groupDoc = transaction.get(groupRef)
                val participants = groupDoc.get("participants") as? List<String> ?: listOf()
                
                if (!participants.contains(userId)) {
                    // Gruba katılımcı olarak ekle
                    transaction.update(groupRef, "participants", participants + userId)
                    
                    // UserChats koleksiyonuna ekle
                    val userChatData = hashMapOf(
                        "lastMessageTimestamp" to Timestamp.now(),
                        "unreadCount" to 0
                    )
                    
                    val userChatRef = firestore.collection("userChats")
                        .document(userId)
                        .collection("chats")
                        .document(groupId)
                    
                    transaction.set(userChatRef, userChatData)
                }
            }.addOnSuccessListener {
                onSuccess()
            }.addOnFailureListener { e ->
                Log.e("GroupsViewModel", "Error joining group", e)
            }
        }
    }

    open fun isUserInGroup(groupId: String): Boolean {
        val group = _groups.value.find { it.id == groupId }
        return group?.participants?.contains(currentUserId) == true
    }

    fun createNewGroup(name: String, participantIds: List<String>) {
        currentUserId?.let { userId ->
            val allParticipants = listOf(userId) + participantIds
            
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
                    // Tüm katılımcılar için userChats koleksiyonuna ekle
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

                    // İlk mesajı ekle
                    val message = hashMapOf(
                        "text" to "Grup oluşturuldu",
                        "senderId" to userId,
                        "timestamp" to Timestamp.now()
                    )

                    newGroupRef.collection("messages").add(message)
                        .addOnSuccessListener {
                            // Son mesajı güncelle
                            newGroupRef.update(
                                mapOf(
                                    "lastMessage" to "Grup oluşturuldu",
                                    "lastMessageTimestamp" to Timestamp.now()
                                )
                            )
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("GroupsViewModel", "Error creating group", e)
                }
        }
    }
}
