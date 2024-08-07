package com.compose.presentation.intents

import com.compose.presentation.models.UserUiModel

sealed class HomeIntent {
    data class  SelectRecentChat(val selectedUser: UserUiModel): HomeIntent()
    data object LoggedOut : HomeIntent()
    data object StartNewChat : HomeIntent()
}