package com.compose.data.repo

import com.compose.data.datasource.user.UserDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository

class UserRepositoryImpl(private val userDataSource: UserDataSource) : UserRepository {

    override suspend fun searchUsers(searchQuery: String, homeUserId: String): List<User> {
        userDataSource.logEvent("attempt_to_search", mapOf("searchQuery" to searchQuery))
        return try {
            // Fetch users by username and filter out the current user
            userDataSource.getUsersByUsername(searchQuery)
                .filter { it.userId != homeUserId }
        } catch (e: Exception) {
            throw Exception("Failed to search users with query: $searchQuery", e)
        }
    }

    override suspend fun getUserById(userId: String): User {
        userDataSource.logEvent("attempt_to_fetch_user", mapOf("userId" to userId))
        return try {
            // Fetch user by ID, throw an exception if user not found
            userDataSource.getUserById(userId)
                ?: throw Exception("User with ID $userId not found.")
        } catch (e: Exception) {
            throw Exception("Failed to retrieve user with ID: $userId", e)
        }
    }

    override suspend fun addUser(user: User) {
        userDataSource.logEvent("attempt_to_add_new_user", mapOf("userId" to user.userId))
        userDataSource.addUser(user)
    }

    override suspend fun saveUserPreferences(user: User) = try {
        userDataSource.logEvent("attempt_to_save_user_preferences", mapOf("userId" to user.userId))
        userDataSource.cacheUser(user)
    } catch (e: Exception) {
        throw Exception("Failed to cache user: ${e.localizedMessage}")
    }

    override suspend fun getCachedUser(): User? = try {
        userDataSource.logEvent("attempt_to_get_user_saved_preferences")
        userDataSource.getCachedUser()
    } catch (e: Exception) {
        throw Exception("Failed to retrieve cached user", e)
    }

    override suspend fun clearUserPreferences() = try {
        userDataSource.logEvent("attempt_to_clear_user_preferences")
        // Perform the clear operation
        userDataSource.clearUser()
    } catch (e: Exception) {
        throw Exception("Failed to clear user preferences: ${e.localizedMessage}", e)
    }
}


