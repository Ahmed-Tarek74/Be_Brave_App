package com.compose.presentation.events

import com.compose.presentation.models.UserUiModel

sealed class SearchUsersEvent {
    data class UserSelected(val awayUser: UserUiModel, val homeUser:UserUiModel):SearchUsersEvent()
    data class BackToHome(val homeUser:UserUiModel) : SearchUsersEvent()
}