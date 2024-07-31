package com.compose.chatapp.presentation.intents

import com.compose.domain.entities.User

sealed class LoginIntent {
    data class Login(val user: User):LoginIntent()
    data object NavigateToRegister:LoginIntent()
    data class EmailChanged(val email:String):LoginIntent()
    data class PasswordChanged(val password:String):LoginIntent()
    data class PasswordVisibilityChanged(val currentVisibility:Boolean):LoginIntent()

}