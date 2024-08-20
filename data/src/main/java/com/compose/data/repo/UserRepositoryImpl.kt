package com.compose.data.repo

import com.compose.data.datasource.user.IUserDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository

class UserRepositoryImpl(private val userDataSource: IUserDataSource) : UserRepository {

    override suspend fun searchUsers(searchQuery: String, homeUserId: String): List<User> {
        return try {
            // Fetch users by username and filter out the current user
            userDataSource.getUsersByUsername(searchQuery)
                .filter { it.userId != homeUserId }
        } catch (e: Exception) {
            throw Exception("Failed to search users with query: $searchQuery", e)
        }
    }

    override suspend fun getUserById(userId: String): User {
        return try {
            // Fetch user by ID, throw an exception if user not found
            userDataSource.getUserById(userId)
                ?: throw Exception("User with ID $userId not found.")
        } catch (e: Exception) {
            throw Exception("Failed to retrieve user with ID: $userId", e)
        }
    }

    override suspend fun addUser(user: User) {
        userDataSource.addUser(user)
    }

    override suspend fun saveUserPreferences(user: User) {
        try {
            userDataSource.cacheUser(user)
        } catch (e: Exception) {
            throw Exception("Failed to cache user: ${e.localizedMessage}")
        }
    }

    override suspend fun getCachedUser(): User {
        try {
            return userDataSource.getCachedUser() ?: throw Exception("Failed to retrieve cached user")
        } catch (e: Exception) {
            throw Exception("Failed to retrieve cached user", e)
        }
    }

    override suspend fun clearUserPreferences() {
        try {
            // Perform the clear operation
            userDataSource.clearUser()
        } catch (e: Exception) {
            throw Exception("Failed to clear user preferences: ${e.localizedMessage}", e)
        }
    }
}


