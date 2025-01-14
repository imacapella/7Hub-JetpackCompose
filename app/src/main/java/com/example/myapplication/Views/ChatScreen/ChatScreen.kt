package com.example.myapplication.Views.ChatScreen

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.R
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
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Arka plan dairesi
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

                // Top Bar içeriği
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Geri butonu
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Geri",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Grup/Chat ismi
                        Text(
                            text = viewModel.chatName,
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        // Sağ tarafta dengeleme için boş alan
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }

            // Content Bölümü
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // Mesajlar
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
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

                // Mesaj Gönderme Alanı
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

@Composable
fun ChatTopAppBar(
    chatName: String,
    photoUrl: String,
    isGroupChat: Boolean,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        // Mavi arka plan
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Constants.hubBlue,
            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
        ) {}

        // İçerik
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            // Üst kısım - Geri butonu ve başlık
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Geri",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Chats",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )

                // Sağ tarafta boş alan bırakmak için
                Spacer(modifier = Modifier.size(32.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Alt kısım - Profil resmi ve chat detayları
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profil resmi
                Surface(
                    modifier = Modifier
                        .size(40.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    AsyncImage(
                        model = photoUrl,
                        contentDescription = if (isGroupChat) "Grup Fotoğrafı" else "Profil Fotoğrafı",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        error = painterResource(id = R.drawable.app_logo)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Chat ismi ve tip göstergesi
                Column {
                    Text(
                        text = chatName,
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    if (isGroupChat) {
                        Text(
                            text = "Grup Sohbeti",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}