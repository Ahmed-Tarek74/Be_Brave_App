package com.compose.presentation.viewStates

import com.compose.domain.entities.User


data class SearchUsersViewState (
    val usersList:List<User> = emptyList(),
    val searchQuery:String="",
    val isLoading:Boolean=false,
    val errorMsg: String?=null,
    val emptyListErrorMsg:String?=null,
    )

