package com.compose.presentation.events

sealed class RegistrationEvent {
    data object RegistrationSuccess :RegistrationEvent()
    data object BackToLogin :RegistrationEvent()
}