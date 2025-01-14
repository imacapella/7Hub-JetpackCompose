package com.example.myapplication.DataLayer.Models

data class ClubModel(
    val clubId: String = "",
    val clubName: String = "",
    val clubIcon: String = "",
    val description: String = "",
    val memberCount: Int = 0,
    val members: List<String> = emptyList(),
    val chatId: String = ""  // Chat ID'sini ekledik
)

enum class ClubTab {
    MY_CLUBS,
    ALL_CLUBS
}