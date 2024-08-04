package com.compose.presentation.intents

import com.compose.domain.entities.User

sealed class HomeIntent {
    data object LoadRecentChats : HomeIntent()
    data class  SelectRecentChat(val selectedUser: User): HomeIntent()
    data object LoggedOut : HomeIntent()
    data object StartNewChat : HomeIntent()
}