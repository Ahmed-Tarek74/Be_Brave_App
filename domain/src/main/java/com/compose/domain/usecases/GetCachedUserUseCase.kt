package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository

class GetCachedUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): User? = userRepository.getCachedUser()
}