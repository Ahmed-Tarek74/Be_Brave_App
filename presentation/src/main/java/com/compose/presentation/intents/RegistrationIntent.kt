package com.compose.presentation.intents

sealed class RegistrationIntent {
    data object Register : RegistrationIntent()
    data object NavigateToLogin : RegistrationIntent()
    data class UsernameChanged(val username: String) : RegistrationIntent()
    data class EmailChanged(val email: String) : RegistrationIntent()
    data class PasswordChanged(val password: String) : RegistrationIntent()
    data class PasswordVisibilityChanged(val currentVisibility: Boolean) : RegistrationIntent()
    data class ConfirmationPasswordChanged(val confirmationPassword: String) : RegistrationIntent()
    data class ConfirmationPasswordVisibilityChanged(val currentVisibility: Boolean) : RegistrationIntent()
}