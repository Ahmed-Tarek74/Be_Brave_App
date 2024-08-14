package com.compose.presentation.events

sealed class RegistrationEvent {
    data object RegisterSuccessfully :RegistrationEvent()
    data object backToLogin :RegistrationEvent()
}