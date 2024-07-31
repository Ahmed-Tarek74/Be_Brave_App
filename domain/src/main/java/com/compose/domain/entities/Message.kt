package com.compose.domain.entities

data class Message (
    val senderId: String="",
    val receiverId: String="",
    val message: String="",
    val timestamp: Long=0
)
