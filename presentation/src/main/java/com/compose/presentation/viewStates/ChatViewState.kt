package com.compose.presentation.viewStates

import com.compose.domain.entities.Message

data class ChatViewState(
    val messagesList: List<Message> = emptyList(),
    val message: String = "",
    val isSendEnabled: Boolean = false,
    val errorMsg: String = "",
    val isLoading: Boolean = false
)