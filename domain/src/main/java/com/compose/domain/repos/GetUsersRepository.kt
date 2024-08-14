package com.compose.domain.repos

import com.compose.domain.entities.User

interface GetUsersRepository {
    suspend fun searchUsers(searchQuery:String, homeUserId:String):List<User>
    suspend fun getUserById(userId:String): User
}