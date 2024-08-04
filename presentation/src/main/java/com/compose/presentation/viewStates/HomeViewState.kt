package com.compose.presentation.viewStates

import com.compose.domain.entities.RecentChat

sealed class HomeViewState {
    data object Loading : HomeViewState()
    data class Success(val recentChats: List<RecentChat>) : HomeViewState()
    data class Failure(val error: String) : HomeViewState()
}