package com.compose.domain.repos

import com.compose.domain.entities.User

interface AuthRepository {
    suspend fun login(email: String, password: String): User

    suspend fun register(user: User):User

    suspend fun logOut()

}