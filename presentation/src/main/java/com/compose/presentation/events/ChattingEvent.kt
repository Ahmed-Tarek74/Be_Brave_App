package com.compose.presentation.events

import com.compose.presentation.models.UserUiModel

sealed class ChattingEvent {
    data class BackToHome(val homeUser: UserUiModel) : ChattingEvent()
}