package com.example.myapplication.Views.ClubsView

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.Utilities.Constants
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.Utilities.ClubUtils

@Composable
fun ClubDetailScreen(
    viewModel: ClubDetailViewModel,
    onBackClick: () -> Unit,
    onJoinClubClick: () -> Unit,
    onJoinError: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        // Mavi üst kısım
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Geri butonu
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Geri",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Kulüp ikonu
            Surface(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                color = Constants.hubGreen
            ) {
                Icon(
                    painter = painterResource(id = ClubUtils.getClubIcon(uiState.clubIcon)),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(24.dp)
                        .size(72.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Kulüp ismi
            Text(
                text = uiState.clubName,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Kulüp açıklaması
            Text(
                text = uiState.clubDescription,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Join Club butonu
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        viewModel.joinClubChat(
                            onSuccess = onJoinClubClick,
                            onError = onJoinError
                        )
                    },
                    modifier = Modifier
                        .width(143.dp)
                        .height(49.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Constants.hubGreen
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !uiState.isJoining
                ) {
                    if (uiState.isJoining) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Join Club",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClubDetailScreenPreview() {
    val previewViewModel = ClubDetailViewModel().apply {
        updateClubDetails(
            clubName = "Cinema Club",
            clubDescription = "Our club is a community that brings together everyone who is passionate about cinema, organizing film screenings, workshops and enjoyable discussions. Our aim is to deeply examine the art of cinema by exploring different types of films, to develop the creative thoughts of our members and to provide a pleasant social environment. Our club organizes events in many areas from film production processes to screenwriting, and keeps its doors open to everyone who wants to have fun and learn!",
            clubIcon = "cinema",
            memberCount = 42
        )
    }
    
    ClubDetailScreen(
        viewModel = previewViewModel,
        onBackClick = {},
        onJoinClubClick = {},
        onJoinError = {}
    )
} 