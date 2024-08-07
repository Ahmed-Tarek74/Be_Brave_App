package com.compose.presentation.models

data class RecentChatUiModel (
    val awayUser: UserUiModel ,
    val recentMessage: String,
    val timestamp: String
)