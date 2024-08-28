package com.compose.data.repo

import com.compose.data.datasource.auth.AuthenticationDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository

class AuthRepositoryImpl(
    private val authDataSource: AuthenticationDataSource,
) : AuthRepository {
    override suspend fun login(email: String, password: String): String {
        authDataSource.logEvent("attempt_to_login")
        return try {
            val userId = authDataSource.login(email, password)
            userId
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message}", e)
        }
    }

    override suspend fun register(user: User): User {
        authDataSource.logEvent("Attempt_to_sign_up")
        return try {
            val userId = authDataSource.register(user.email, user.password)
            val registeredUser = user.copy(userId = userId)
            registeredUser
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message}", e)
        }
    }

    override suspend fun logOut() {
        authDataSource.logEvent("attempt_to_logout")
        try {
            authDataSource.logout()
        } catch (e: Exception) {
            throw Exception("Logout failed: ${e.message}", e)
        }
    }
}