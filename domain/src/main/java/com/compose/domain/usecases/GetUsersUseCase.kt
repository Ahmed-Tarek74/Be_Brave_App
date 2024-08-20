package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository

class GetUsersUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(query: String, userId: String): List<User> {
        return userRepository.searchUsers(query, userId)
    }
}