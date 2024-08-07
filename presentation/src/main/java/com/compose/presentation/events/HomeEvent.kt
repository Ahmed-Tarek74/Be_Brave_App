package com.compose.presentation.events
import com.compose.presentation.models.UserUiModel

sealed class HomeEvent {
    data object NavigateToLoginScreen: HomeEvent()
    data class NavigateToSearchScreen(val homeUser:UserUiModel) : HomeEvent()
    data class NavigateToChattingScreen(val awayUser: UserUiModel , val homeUser:UserUiModel):HomeEvent()
}