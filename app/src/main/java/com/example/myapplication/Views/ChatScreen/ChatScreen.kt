package com.example.myapplication.Views.ChatScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.myapplication.Utilities.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    chatId: String,
    onNavigateBack: () -> Unit
) {
    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    var messageText by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        viewModel.loadMessages(chatId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Constants.hubBlue)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }

                    // Profil resmi
                    Image(
                        painter = rememberAsyncImagePainter(model = viewModel.chatUserPhotoUrl),
                        contentDescription = "Profil Resmi",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = viewModel.chatName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 0.dp)
        ) {
            // Mesajlar
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(items = messages) { message ->
                    MessageItem(
                        message = message,
                        isOwnMessage = message.senderId == viewModel.currentUserId
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Mesaj gönderme alanı
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Mesaj", color = Constants.hubDark) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedBorderColor = Constants.hubBabyBlue,
                        focusedBorderColor = Constants.hubGreen
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            viewModel.sendMessage(chatId, messageText)
                            messageText = ""
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Gönder",
                        tint = Constants.hubGreen
                    )
                }
            }
        }
    }
}

@Composable
fun MessageItem(
    message: ChatMessage,
    isOwnMessage: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isOwnMessage) 16.dp else 0.dp,
                        bottomEnd = if (isOwnMessage) 0.dp else 16.dp
                    )
                )
                .background(
                    if (isOwnMessage) Constants.hubBabyBlue
                    else Color.LightGray
                )
                .padding(12.dp)
        ) {
            Text(
                text = message.text,
                color = if (isOwnMessage) Color.White else Constants.hubDark,
                modifier = Modifier.widthIn(max = 260.dp)
            )
        }
        Text(
            text = message.timestamp,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = if (isOwnMessage) TextAlign.End else TextAlign.Start
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    val fakeMessages = listOf(
        ChatMessage(
            id = "1",
            text = "Merhaba, ödev hakkında konuşabilir miyiz?",
            senderId = "2",
            timestamp = "10:30"
        ),
        ChatMessage(
            id = "2",
            text = "Tabii ki, hangi konu hakkında yardıma ihtiyacın var?",
            senderId = "1", // current user
            timestamp = "10:31"
        ),
        ChatMessage(
            id = "3",
            text = "VCD 471 projesi için kaynak araştırmaları yapıyorum da",
            senderId = "2",
            timestamp = "10:32"
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            // Top Bar
            TopAppBar(
                title = { Text("Ahmet Yılmaz", color = Constants.hubDark) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Constants.hubDark)
                    }
                }
            )

            // Messages
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                reverseLayout = true
            ) {
                items(items = fakeMessages) { message ->
                    MessageItem(
                        message = message,
                        isOwnMessage = message.senderId == "1"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Message Input
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Mesaj", color = Constants.hubDark) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent,
                        unfocusedBorderColor = Constants.hubBabyBlue,
                        focusedBorderColor = Constants.hubGreen
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                IconButton(onClick = {}) {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Gönder",
                        tint = Constants.hubGreen
                    )
                }
            }
        }
    }
}
