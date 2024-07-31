package com.compose.domain.repos

import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    fun getCachedUser(): Flow<User?>
    suspend fun saveUserPreferences(user: User)
    suspend fun clearUserPreferences()
}