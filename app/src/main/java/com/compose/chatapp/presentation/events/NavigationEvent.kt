package com.compose.chatapp.presentation.events

import android.os.Bundle
import com.compose.domain.entities.User

sealed class NavigationEvent {
    data object NavigateToLoginScreen: NavigationEvent()
    data class NavigateToSearchScreen(val homeUser:User) : NavigationEvent()
    data class NavigateToChattingScreen(val awayUser: User , val homeUser:User):NavigationEvent()
    data class NavigateToHome(val homeUser:User) : NavigationEvent()
    data object NavigateToRegistration : NavigationEvent()
    data class To(val destination: Int, val args: Bundle? = null) : NavigationEvent()
}