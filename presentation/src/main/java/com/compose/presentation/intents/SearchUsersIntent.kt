package com.compose.presentation.intents
import com.compose.presentation.models.UserUiModel

sealed class SearchUsersIntent {
    data object BackToHome :SearchUsersIntent()
    data class UpdateSearchQuery(val searchQuery:String):SearchUsersIntent()
    data class SelectUser(val user:UserUiModel):SearchUsersIntent()
}