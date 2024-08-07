package com.compose.presentation.events

import com.compose.presentation.models.UserUiModel

sealed class LoginEvent {
    data object NavigateToRegistration : LoginEvent()
    data class LoginSuccess(val homeUser: UserUiModel) : LoginEvent()
}