package com.compose.presentation.intents

sealed class LoginIntent {
    data object Login:LoginIntent()
    data object NavigateToRegister:LoginIntent()
    data class EmailChanged(val email:String):LoginIntent()
    data class PasswordChanged(val password:String):LoginIntent()
    data class PasswordVisibilityChanged(val currentVisibility:Boolean):LoginIntent()

}