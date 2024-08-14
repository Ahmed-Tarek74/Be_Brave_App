package com.compose.domain.usecases

import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogOutUseCase(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {

        // Perform logout operation
        authRepository.logOut()
        // Clear user preferences
        userPreferencesRepository.clearUserPreferences()
    }
}