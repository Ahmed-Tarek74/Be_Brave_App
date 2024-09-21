package com.compose.presentation.viewStates
import com.compose.presentation.R
import com.compose.presentation.models.MessageUiModel

data class ChatViewState(
    val messagesList: List<MessageUiModel> = emptyList(),
    val message: String = "",
    val isSendEnabled: Boolean = false,
    val errorMsg: String? = null,
    val isLoading: Boolean = false,
    val sendBtnContainerColor :Int = R.color.gray
)
