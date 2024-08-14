package com.compose.domain.repos

import com.compose.domain.entities.User

interface UserPreferencesRepository {

    suspend fun getCachedUser(): User
    suspend fun saveUserPreferences(user: User)
    suspend fun clearUserPreferences()
}