package com.compose.presentation.viewStates
import com.compose.presentation.models.UserUiModel


data class SearchUsersViewState (
    val usersList:List<UserUiModel> = emptyList(),
    val searchQuery:String="",
    val isLoading:Boolean=false,
    val errorMsg: String?=null,
    val emptyListErrorMsg:String?=null,
    )

