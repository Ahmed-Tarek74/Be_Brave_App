package com.compose.data.repo

import com.compose.data.datasource.user.UserDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository

class GetUsersRepositoryImpl(private val userDataSource: UserDataSource) : GetUsersRepository {
    override suspend fun searchUsers(searchQuery: String, homeUserId: String): List<User> {
        return try {
            // Fetch users by username and filter out the current user
            userDataSource.getUsersByUsername(searchQuery).filter { it.userId != homeUserId }
        } catch (e: Exception) {
            // Handle any potential exceptions and provide meaningful context
            throw Exception("Failed to search users with query: $searchQuery", e)
        }
    }

    override suspend fun getUserById(userId: String): User {
        return try {
            // Fetch user by ID, throw an exception if user not found
            userDataSource.getUserById(userId) ?: throw Exception("User with ID $userId not found.")
        } catch (e: Exception) {
            // Handle any potential exceptions and provide meaningful context
            throw Exception("Failed to retrieve user with ID: $userId", e)
        }
    }
}


