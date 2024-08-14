package com.compose.data.datasource.user

import com.compose.domain.entities.User

interface UserDataSource {
    suspend fun getUserById(userId: String): User?
    suspend fun getUsersByUsername(searchQuery:String):List<User>
    suspend fun saveUser(user: User)
}