package com.compose.presentation.viewStates
import com.compose.presentation.models.RecentChatUiModel

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data class Success(val recentChats: List<RecentChatUiModel>) : HomeViewState()
    data class Failure(val error: String) : HomeViewState()
}