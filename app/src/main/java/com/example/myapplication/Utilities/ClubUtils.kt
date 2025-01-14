package com.example.myapplication.Utilities

import com.example.myapplication.R

object ClubUtils {
    fun getClubIcon(iconName: String): Int {
        return when (iconName.lowercase()) {
            "cinema" -> R.drawable.ic_cinema
            "theatre" -> R.drawable.ic_theatre
            "music" -> R.drawable.ic_music
            else -> R.drawable.ic_default_club
        }
    }
} 