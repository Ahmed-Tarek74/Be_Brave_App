package com.compose.domain.repos

import com.compose.domain.entities.User

interface UserRepository {
    suspend fun searchUsers(searchQuery: String, homeUserId: String): List<User>
    suspend fun getUserById(userId: String): User
    suspend fun addUser(user: User)
    suspend fun getCachedUser(): User?
    suspend fun saveUserPreferences(user: User)
    suspend fun clearUserPreferences()
}