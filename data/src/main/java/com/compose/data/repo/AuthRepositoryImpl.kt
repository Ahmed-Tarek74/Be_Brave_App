package com.compose.data.repo

import com.compose.data.datasource.auth.IAuthentication
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository

class AuthRepositoryImpl(
    private val firebaseAuth: IAuthentication,
) : AuthRepository {
    override suspend fun login(email: String, password: String): String {
        return try {
            val userId =firebaseAuth.login(email, password)
            userId
        } catch (e: Exception) {
            throw Exception("Login failed: ${e.message}", e)
        }
    }

    override suspend fun register(user: User): User {
        return try {
            val userId = firebaseAuth.register(user.email, user.password)
            val registeredUser = user.copy(userId = userId)
            registeredUser
        } catch (e: Exception) {
            throw Exception("Registration failed: ${e.message}", e)
        }
    }

    override suspend fun logOut() {
        try {
            firebaseAuth.logout()
        } catch (e: Exception) {
            throw Exception("Logout failed: ${e.message}", e)
        }
    }
}