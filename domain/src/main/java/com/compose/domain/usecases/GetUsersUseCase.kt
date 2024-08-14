package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository

class GetUsersUseCase(private val getUsersRepository: GetUsersRepository) {

    suspend fun searchUsers(query: String, userId: String): List<User> {
        return getUsersRepository.searchUsers(query, userId)
    }
}