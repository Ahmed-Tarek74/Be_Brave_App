package com.compose.domain.entities

data class RecentChat(
    val awayUser: User=User(),
    val recentMessage: String="",
    val timestamp: Long=0
)
