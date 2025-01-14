package com.example.myapplication.Views.ChatList

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.DataLayer.Models.ChatData
import com.example.myapplication.DataLayer.Models.UserData
import com.example.myapplication.R
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import coil.compose.AsyncImage
import com.example.myapplication.Utilities.Constants
import java.util.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.ViewModelStoreOwner
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onChatClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: ChatListViewModel = viewModel()
    val chats by viewModel.chats.observeAsState(emptyList())
    var showNewChatDialog by remember { mutableStateOf(false) }
    val users by viewModel.users.observeAsState(emptyList())
    var selectedTab by remember { mutableStateOf("Private Chats") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // TitleCircle
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(380.dp)
        ) {
            val circleRadius = 800.dp.toPx()
            val circleCenterX = size.width / 2
            val circleCenterY = -circleRadius + 210f

            drawCircle(
                color = Constants.hubBlue,
                radius = circleRadius,
                center = Offset(circleCenterX, circleCenterY)
            )
        }

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            TopAppBar(
                title = { 
                    Text(
                        "Chats", 
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 100.dp)
                    ) 
                },
                navigationIcon = {
                    IconButton(
                        onClick = { /* Geri git */ },
                        modifier = Modifier
                            .padding(start = 0.dp, top = 0.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack, 
                            contentDescription = "Geri", 
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .height(48.dp)
                    .padding(top = 0.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tabs with elevation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TabButton(
                    text = "Private Chats",
                    selected = selectedTab == "Private Chats",
                    onClick = { selectedTab = "Private Chats" }
                )
                Spacer(modifier = Modifier.width(16.dp))
                TabButton(
                    text = "Group Chats",
                    selected = selectedTab == "Group Chats",
                    onClick = { selectedTab = "Group Chats" }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chat listesi
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(chats.filter {
                    when (selectedTab) {
                        "Private Chats" -> it.chatType == "PRIVATE"
                        "Group Chats" -> it.chatType == "GROUP"
                        else -> true
                    }
                }) { chat ->
                    ChatListItem(
                        chat = chat,
                        onClick = { onChatClick(chat.chatId) },
                        onDeleteClick = { viewModel.deleteChat(chat.chatId) }
                    )
                }
            }
        }

        // FAB - Sadece Private Chats tab'i seçiliyken göster
        if (selectedTab == "Private Chats") {
            FloatingActionButton(
                onClick = { showNewChatDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = Constants.hubBabyBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Yeni Sohbet", tint = Color.White)
            }
        }
    }

    if (showNewChatDialog) {
        NewChatDialog(
            users = users,
            onDismiss = { showNewChatDialog = false },
            onUserSelected = { user ->
                viewModel.createNewChat(user)
                showNewChatDialog = false
            },
            selectedTab = selectedTab,
            onGroupCreate = { users, groupName ->
                viewModel.createGroupChat(users, groupName)
            }
        )
    }
}

@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(60.dp)
            .width(180.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                Log.d("ButtonClicked", "$text button clicked")
                onClick()
            },
            modifier = Modifier
                .height(if (selected) 46.dp else 42.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF3F3F3)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 6.dp
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = Constants.hubGreen,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun ChatListItem(
    chat: ChatData,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(color = Constants.hubBabyBlue, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.chatName.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = chat.chatName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Constants.hubDark
                )
                Text(
                    text = chat.lastMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        IconButton(onClick = { showDeleteDialog = true }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Sohbeti Sil",
                tint = Color.Gray
            )
        }
    }
    
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Sohbeti Sil") },
            text = { Text("Bu sohbeti silmek istediğinizden emin misiniz?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    }
                ) {
                    Text("Evet")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Hayır")
                }
            }
        )
    }

    Divider(
        modifier = Modifier.padding(start = 82.dp),
        color = Color.LightGray,
        thickness = 0.5.dp
    )
}

fun formatTimestamp(timestamp: Timestamp): String {
    val date = timestamp.toDate()
    val now = Date()
    val diff = now.time - date.time
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "${days.toInt()} gün önce"
        hours > 0 -> "${hours.toInt()} saat önce"
        minutes > 0 -> "${minutes.toInt()} dakika önce"
        else -> "Şimdi"
    }
}

@Composable
fun NewChatDialog(
    users: List<UserData>,
    onDismiss: () -> Unit,
    onUserSelected: (UserData) -> Unit,
    selectedTab: String,
    onGroupCreate: (List<String>, String) -> Unit
) {
    var selectedUsers by remember { mutableStateOf(setOf<UserData>()) }
    var groupName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (selectedTab == "Private Chats") "Yeni Sohbet" else "Yeni Grup") },
        text = {
            Column {
                if (selectedTab == "Group Chats") {
                    OutlinedTextField(
                        value = groupName,
                        onValueChange = { groupName = it },
                        label = { Text("Grup Adı") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                }
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp)
                ) {
                    items(users) { user ->
                        UserListItem(
                            user = user,
                            isSelected = selectedUsers.contains(user),
                            onClick = {
                                if (selectedTab == "Private Chats") {
                                    onUserSelected(user)
                                } else {
                                    selectedUsers = if (selectedUsers.contains(user)) {
                                        selectedUsers - user
                                    } else {
                                        selectedUsers + user
                                    }
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            if (selectedTab == "Group Chats") {
                TextButton(
                    onClick = {
                        if (groupName.isNotBlank() && selectedUsers.isNotEmpty()) {
                            onGroupCreate(selectedUsers.map { it.uid }, groupName)
                            onDismiss()
                        }
                    },
                    enabled = groupName.isNotBlank() && selectedUsers.isNotEmpty()
                ) {
                    Text("Grup Oluştur")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

@Composable
fun UserListItem(
    user: UserData,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.displayName.firstOrNull()?.uppercase() ?: "?",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = user.displayName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seçildi",
                    tint = Constants.hubGreen
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun ChatListScreenPreview() {
    val fakeChatList = listOf(
        ChatData(
            chatId = "1",
            chatName = "Ahmet Yılmaz",
            chatType = "PRIVATE",
            lastMessage = "Merhaba, ödev hakkında konuşabilir miyiz?",
            lastMessageTimestamp = Timestamp(Date()),
            participants = listOf("1", "2"),
            participantNames = mapOf("1" to "Ben", "2" to "Ahmet Yılmaz")
        ),
        ChatData(
            chatId = "2",
            chatName = "VCD 471 Grup",
            chatType = "GROUP",
            lastMessage = "Proje teslim tarihi ne zaman?",
            lastMessageTimestamp = Timestamp(Date(System.currentTimeMillis() - 3600000)),
            participants = listOf("1", "2", "3"),
            participantNames = mapOf("1" to "Ben", "2" to "Ali", "3" to "Ayşe")
        ),
        ChatData(
            chatId = "3",
            chatName = "Merve Çaşkurlu",
            chatType = "PRIVATE",
            lastMessage = "Teşekkür ederim",
            lastMessageTimestamp = Timestamp(Date(System.currentTimeMillis() - 7200000)),
            participants = listOf("1", "4"),
            participantNames = mapOf("1" to "Ben", "4" to "Merve Çaşkurlu")
        )
    )

    MaterialTheme {
        Surface {
            ChatListScreen(
                onChatClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}



