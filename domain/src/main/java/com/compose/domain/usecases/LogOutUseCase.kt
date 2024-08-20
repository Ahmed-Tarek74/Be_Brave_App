package com.compose.domain.usecases

import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogOutUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO){
        // Perform logout operation
        authRepository.logOut()
        // Clear user preferences
        userRepository.clearUserPreferences()
    }
}