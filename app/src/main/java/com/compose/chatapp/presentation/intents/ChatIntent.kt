package com.compose.chatapp.presentation.intents

sealed class ChatIntent {
    data object BackToHome:ChatIntent()
    data class MessageInputChanged(val messageInput: String):ChatIntent()
    data class SendMessage(val message:String):ChatIntent()
}