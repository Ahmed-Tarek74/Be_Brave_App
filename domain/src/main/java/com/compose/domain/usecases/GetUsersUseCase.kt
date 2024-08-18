package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository

class GetUsersUseCase(private val getUsersRepository: GetUsersRepository) {

    suspend operator fun invoke(query: String, userId: String): List<User> {
        return getUsersRepository.searchUsers(query, userId)
    }
}