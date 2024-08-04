package com.compose.presentation.viewStates

data class RegistrationViewState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmationPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmationPasswordVisible: Boolean = false,
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
