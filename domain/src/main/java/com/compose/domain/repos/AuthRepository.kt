package com.compose.domain.repos

import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(user: User): Flow<Result<User>>

    fun register(user:User): Flow<Result<User>>

    suspend fun logOut()

}