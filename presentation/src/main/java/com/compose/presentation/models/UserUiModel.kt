package com.compose.presentation.models

import java.io.Serializable

data class UserUiModel(
    val userId: String,
    val username: String,
    val profilePicture: Any
): Serializable
