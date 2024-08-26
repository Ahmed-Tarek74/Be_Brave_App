package com.compose.presentation.events

import com.compose.presentation.models.UserUiModel

sealed class UserCacheEvent {
    data class UserLoaded(val isUserLoggedIn: Boolean, val user: UserUiModel? = null) : UserCacheEvent()
    data class UserLoadFailed(val errorMsg: String) : UserCacheEvent()
}