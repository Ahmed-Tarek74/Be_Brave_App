package com.compose.domain.repos

import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface GetUsersRepository {
    fun searchUsers(searchQuery:String, homeUserId:String):Flow<List<User>>
    fun getUserById(userId:String): Flow<User?>
}