package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserPreferencesRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        val user = authRepository.login(email, password)
        userPreferencesRepository.saveUserPreferences(user)
        val token=deviceTokenRepository.getDeviceToken()
        deviceTokenRepository.saveDeviceToken(user.userId,token)
        return user
    }
}