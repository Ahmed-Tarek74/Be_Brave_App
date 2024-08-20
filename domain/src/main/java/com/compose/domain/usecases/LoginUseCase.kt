package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User {
        val userId = authRepository.login(email, password)
        val user = userRepository.getUserById(userId)
        userRepository.saveUserPreferences(user)
        deviceTokenRepository.setDeviceTokenToUser(userId)
        return user
    }
}