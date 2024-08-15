package com.compose.data.datasource.user

import com.compose.domain.entities.User

interface IUserPreferencesDataSource {
    suspend fun cacheUser(user: User)
    suspend fun clearUser()
    suspend fun getUser(): User?
}