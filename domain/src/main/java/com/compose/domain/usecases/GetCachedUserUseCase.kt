package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetCachedUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): User =
        withContext(Dispatchers.IO) {
            userRepository.getCachedUser()
        }
}