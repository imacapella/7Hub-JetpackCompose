package com.example.myapplication.Views.ClubsView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ClubDetailUiState(
    val clubName: String = "",
    val clubDescription: String = "",
    val clubIcon: String = "",
    val memberCount: Int = 0,
    val isJoining: Boolean = false
)

class ClubDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ClubDetailUiState())
    val uiState: StateFlow<ClubDetailUiState> = _uiState.asStateFlow()
    
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun joinClubChat(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isJoining = true)
                val currentUser = auth.currentUser
                
                if (currentUser == null) {
                    onError("Kullanıcı oturumu bulunamadı")
                    return@launch
                }

                // 1. Önce kullanıcıyı klübe ekleyelim
                val clubQuery = db.collection("clubs")
                    .whereEqualTo("name", _uiState.value.clubName)
                    .get()
                    .await()

                if (!clubQuery.isEmpty) {
                    val clubDoc = clubQuery.documents.first()
                    val clubMembers = clubDoc.get("members") as? List<String> ?: listOf()
                    
                    if (!clubMembers.contains(currentUser.uid)) {
                        // Kullanıcıyı klübe ekle
                        db.collection("clubs")
                            .document(clubDoc.id)
                            .update("members", clubMembers + currentUser.uid)
                            .await()
                    }

                    // 2. Chat grubunu kontrol et
                    val chatRef = db.collection("chats").document(clubDoc.id)
                    val chatDoc = chatRef.get().await()

                    if (!chatDoc.exists()) {
                        // Chat yoksa yeni oluştur
                        val newChat = hashMapOf(
                            "name" to _uiState.value.clubName,
                            "type" to "GROUP",
                            "participants" to listOf(currentUser.uid),
                            "lastMessage" to "Grup oluşturuldu",
                            "lastMessageTimestamp" to com.google.firebase.Timestamp.now(),
                            "createdAt" to com.google.firebase.Timestamp.now()
                        )
                        
                        chatRef.set(newChat).await()

                        // İlk mesajı ekle
                        val message = hashMapOf(
                            "text" to "Grup oluşturuldu",
                            "senderId" to currentUser.uid,
                            "timestamp" to com.google.firebase.Timestamp.now()
                        )

                        chatRef.collection("messages").add(message).await()
                    } else {
                        // Mevcut chat'e kullanıcıyı ekle
                        val participants = chatDoc.get("participants") as? List<String> ?: listOf()
                        
                        if (!participants.contains(currentUser.uid)) {
                            chatRef.update("participants", participants + currentUser.uid).await()
                        }
                    }

                    // 3. UserChats koleksiyonuna ekle
                    val userChatData = hashMapOf(
                        "lastMessageTimestamp" to com.google.firebase.Timestamp.now(),
                        "unreadCount" to 0
                    )

                    db.collection("userChats")
                        .document(currentUser.uid)
                        .collection("chats")
                        .document(clubDoc.id)  // club ID'sini chat ID olarak kullan
                        .set(userChatData)
                        .await()

                    onSuccess()
                } else {
                    onError("Kulüp bulunamadı")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Bir hata oluştu")
            } finally {
                _uiState.value = _uiState.value.copy(isJoining = false)
            }
        }
    }

    fun updateClubDetails(
        clubName: String,
        clubDescription: String,
        clubIcon: String,
        memberCount: Int
    ) {
        _uiState.value = ClubDetailUiState(
            clubName = clubName,
            clubDescription = clubDescription,
            clubIcon = clubIcon,
            memberCount = memberCount
        )
    }
} 