package com.compose.domain.entities

data class NotificationData(
    val data: Map<String, String>?,
    val notification: Notification?,
    val token: String,
)