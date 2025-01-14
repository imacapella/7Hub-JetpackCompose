package com.example.myapplication.Views.ClubsView

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.DataLayer.Models.ClubModel
import com.example.myapplication.DataLayer.Models.ClubTab
import com.example.myapplication.R
import com.example.myapplication.Utilities.ClubUtils
import com.example.myapplication.Utilities.Constants
import com.example.myapplication.Views.ChatList.TabButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubsScreen(
    viewModel: ClubsViewModel = viewModel(),
    onNavigateBack: () -> Unit = {},
    onClubClick: (ClubModel) -> Unit = {}
) {
    val clubs by viewModel.clubs.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Constants.hubWhite)
    ) {
        // Top Bar
        TopAppBar(
            title = {
                Text(
                    text = "Clubs",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start = 110.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color(0xFFF3F3F3)
            )
        )

        // Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TabButton(
                text = "My Clubs",
                selected = selectedTab == ClubTab.MY_CLUBS,
                onClick = { viewModel.onTabSelected(ClubTab.MY_CLUBS) }
            )
            TabButton(
                text = "All Clubs",
                selected = selectedTab == ClubTab.ALL_CLUBS,
                onClick = { viewModel.onTabSelected(ClubTab.ALL_CLUBS) }
            )
        }

        // Content
        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Constants.hubGreen
                    )
                }
            }
            error != null -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = error ?: "",
                        color = Color.Red,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(clubs) { club ->
                        ClubCard(
                            club = club,
                            onClick = { onClubClick(club) },
                            showArrow = selectedTab == ClubTab.MY_CLUBS
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TabButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(46.dp)
            .width(196.dp)
            .padding(horizontal = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF3F3F3)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 6.dp
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = text,
            color = if (selected) Constants.hubGreen else Constants.hubGreen,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun ClubCard(
    club: ClubModel,
    onClick: () -> Unit,
    showArrow: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.dp)
            .padding(5.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Constants.hubWhite
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Club Icon/Badge
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Constants.hubBabyBlue
            ) {
                Icon(
                    painter = painterResource(id = ClubUtils.getClubIcon(club.clubIcon)),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(12.dp)
                        .size(24.dp)
                )
            }

            // Club Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = club.clubName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Arrow Icon
            if (showArrow) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Go to Chat",
                    tint = Constants.hubGreen
                )
            }
        }
    }
}