package com.example.myapplication.Views.ChatList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.DataLayer.Models.ChatData
import com.example.myapplication.DataLayer.Models.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldPath
import java.util.Date

class ChatListViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val currentUserId = Firebase.auth.currentUser?.uid

    private val _chats = MutableLiveData<List<ChatData>>()
    val chats: LiveData<List<ChatData>> = _chats

    private val _users = MutableLiveData<List<UserData>>()
    val users: LiveData<List<UserData>> = _users

    init {
        loadChats()
        loadUsers()
    }

    private fun loadChats() {
        currentUserId?.let { userId ->
            firestore.collection("userChats")
                .document(userId)
                .collection("chats")
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        return@addSnapshotListener
                    }

                    val chatIds = snapshot?.documents?.map { it.id } ?: emptyList()
                    
                    if (chatIds.isEmpty()) {
                        _chats.value = emptyList()
                        return@addSnapshotListener
                    }

                    firestore.collection("chats")
                        .whereIn(FieldPath.documentId(), chatIds)
                        .get()
                        .addOnSuccessListener { chatsSnapshot ->
                            val chatsList = chatsSnapshot.documents.mapNotNull { doc ->
                                val data = doc.data ?: return@mapNotNull null
                                ChatData(
                                    chatId = doc.id,
                                    chatName = data["name"] as? String ?: "",
                                    chatType = data["type"] as? String ?: "",
                                    lastMessage = data["lastMessage"] as? String ?: "",
                                    lastMessageTimestamp = data["lastMessageTimestamp"] as? Timestamp,
                                    participants = (data["participants"] as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                                )
                            }
                            _chats.value = chatsList
                        }
                }
        }
    }

    private fun loadUsers() {
        firestore.collection("users")
            .whereNotEqualTo(FieldPath.documentId(), currentUserId)
            .get()
            .addOnSuccessListener { snapshot ->
                val usersList = snapshot.documents.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    UserData(
                        uid = doc.id,
                        displayName = data["displayName"] as? String ?: "",
                        email = data["email"] as? String ?: "",
                        photoUrl = data["photoUrl"] as? String ?: ""
                    )
                }
                _users.value = usersList
            }
    }

    fun createNewChat(selectedUser: UserData) {
        currentUserId?.let { userId ->
            firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener { currentUserDoc ->
                    val currentUserName = currentUserDoc.getString("displayName") ?: "Anonim"
                    
                    val chatData = hashMapOf(
                        "type" to "PRIVATE",
                        "name" to selectedUser.displayName,
                        "createdAt" to Timestamp.now(),
                        "lastMessage" to "",
                        "lastMessageTimestamp" to Timestamp.now(),
                        "participants" to listOf(userId, selectedUser.uid),
                        "participantNames" to mapOf(
                            userId to currentUserName,
                            selectedUser.uid to selectedUser.displayName
                        )
                    )

                    firestore.collection("chats")
                        .add(chatData)
                        .addOnSuccessListener { chatRef ->
                            val chatId = chatRef.id
                            
                            val userChatData = hashMapOf(
                                "lastMessageTimestamp" to Timestamp.now(),
                                "unreadCount" to 0
                            )

                            firestore.collection("userChats")
                                .document(userId)
                                .collection("chats")
                                .document(chatId)
                                .set(userChatData)

                            firestore.collection("userChats")
                                .document(selectedUser.uid)
                                .collection("chats")
                                .document(chatId)
                                .set(userChatData)
                        }
                }
        }
    }

    fun createGroupChat(participantIds: List<String>, groupName: String) {
        currentUserId?.let { userId ->
            val allParticipants = listOf(userId) + participantIds
            
            val chatData = hashMapOf(
                "type" to "GROUP",
                "name" to groupName,
                "createdAt" to Timestamp.now(),
                "lastMessage" to "",
                "lastMessageTimestamp" to Timestamp.now(),
                "participants" to allParticipants
            )

            firestore.collection("chats")
                .add(chatData)
                .addOnSuccessListener { chatRef ->
                    val chatId = chatRef.id

                    val userChatData = hashMapOf(
                        "lastMessageTimestamp" to Timestamp.now(),
                        "unreadCount" to 0
                    )

                    allParticipants.forEach { participantId ->
                        firestore.collection("userChats")
                            .document(participantId)
                            .collection("chats")
                            .document(chatId)
                            .set(userChatData)
                    }

                    val message = hashMapOf(
                        "text" to "Grup oluşturuldu",
                        "senderId" to userId,
                        "timestamp" to Timestamp.now(),
                        "type" to "TEXT",
                        "status" to "SENT"
                    )

                    chatRef.collection("messages").add(message)
                }
        }
    }
} 