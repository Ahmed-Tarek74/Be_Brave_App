package com.compose.data.repo

import com.compose.data.datasource.auth.IAuthDataSource
import com.compose.data.datasource.user.UserDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: IAuthDataSource,
    private val userDataSource: UserDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): User {
        return try {
            val userId = authDataSource.login(email, password)
            userDataSource.getUserById(userId) ?: throw Exception("User not found.")
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message}", e)
        }
    }
    override suspend fun register(user: User): User {
        return try {
            val userId = authDataSource.register(user.email, user.password)
            val registeredUser = user.copy(userId = userId)
            userDataSource.saveUser(registeredUser)
            registeredUser
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message}", e)
        }
    }
    override suspend fun logOut() {
        try {
            authDataSource.logout()
        } catch (e: Exception) {
            throw Exception("Logout failed: ${e.message}", e)
        }
    }
}