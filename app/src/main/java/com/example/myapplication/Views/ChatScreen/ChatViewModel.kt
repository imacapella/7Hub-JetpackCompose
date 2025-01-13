package com.example.myapplication.Views.ChatScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

data class ChatMessage(
    val id: String,
    val text: String,
    val senderId: String,
    val timestamp: String
)

class ChatViewModel : ViewModel() {
    private val firestore = Firebase.firestore
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages
    val currentUserId = Firebase.auth.currentUser?.uid
    var chatName: String = ""
        private set
    var chatUserPhotoUrl: String = ""
        private set
    var isGroupChat: Boolean = false
        private set

    fun loadMessages(chatId: String) {
        // Önce chat bilgilerini al
        firestore.collection("chats").document(chatId).get()
            .addOnSuccessListener { document ->
                chatName = document.getString("name") ?: ""
                isGroupChat = document.getBoolean("isGroup") ?: false
                // Chat'teki diğer kullanıcının ID'sini al
                val participants = document.get("participants") as? List<String>
                val otherUserId = participants?.find { it != currentUserId }
                
                // Diğer kullanıcının profil resmini al
                otherUserId?.let { userId ->
                    firestore.collection("users").document(userId).get()
                        .addOnSuccessListener { userDoc ->
                            chatUserPhotoUrl = userDoc.getString("photoUrl") ?: ""
                        }
                }
            }

        // Mesajları dinle
        firestore.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ChatViewModel", "Error loading messages", error)
                    return@addSnapshotListener
                }

                val messageList = snapshot?.documents?.mapNotNull { doc ->
                    val data = doc.data ?: return@mapNotNull null
                    val timestamp = data["timestamp"] as? Timestamp
                    val text = data["text"] as? String ?: return@mapNotNull null
                    val senderId = data["senderId"] as? String ?: return@mapNotNull null

                    ChatMessage(
                        id = doc.id,
                        text = text,
                        senderId = senderId,
                        timestamp = formatTimestamp(timestamp)
                    )
                } ?: emptyList()

                Log.d("ChatViewModel", "Loaded messages: ${messageList.size}")
                _messages.value = messageList
            }
    }

    fun sendMessage(chatId: String, text: String) {
        if (text.isBlank()) return

        val currentTime = Timestamp.now()
        val message = hashMapOf(
            "text" to text,
            "senderId" to (currentUserId ?: return),
            "timestamp" to currentTime
        )

        Log.d("ChatViewModel", "Sending message: $text")

        firestore.collection("chats").document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener { messageRef ->
                Log.d("ChatViewModel", "Message sent successfully")
                
                // Son mesajı güncelle
                firestore.collection("chats").document(chatId)
                    .update(
                        mapOf(
                            "lastMessage" to text,
                            "lastMessageTimestamp" to currentTime
                        )
                    )

                // userChats koleksiyonunda son mesaj zamanını güncelle
                firestore.collection("chats").document(chatId)
                    .get()
                    .addOnSuccessListener { chatDoc ->
                        val participants = chatDoc.get("participants") as? List<String> ?: return@addOnSuccessListener
                        participants.forEach { userId ->
                            firestore.collection("userChats")
                                .document(userId)
                                .collection("chats")
                                .document(chatId)
                                .update(
                                    mapOf(
                                        "lastMessageTimestamp" to currentTime,
                                        "lastMessage" to text
                                    )
                                )
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.e("ChatViewModel", "Error sending message", e)
            }
    }

    private fun formatTimestamp(timestamp: Timestamp?): String {
        if (timestamp == null) return ""
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(timestamp.toDate())
    }
}