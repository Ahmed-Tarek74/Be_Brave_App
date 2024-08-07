package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(
    private val authRepository: AuthRepository,
    private val deviceTokenRepository: DeviceTokenRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(email: String,password:String): Flow<Result<User>> = flow {
            // Attempt to login
            authRepository.login(email,password).collect { loginResult ->
                if (loginResult.isSuccess) {
                    val loggedInUser = loginResult.getOrNull()!!
                    // If login is successful, save the device token and cache user
                    userPreferencesRepository.saveUserPreferences(loggedInUser)
                    deviceTokenRepository.getAndSaveDeviceToken(loggedInUser.userId)
                        .collect { deviceTokenResult ->
                            if (deviceTokenResult) {
                                emit(Result.success(loggedInUser))
                            } else {
                                emit(Result.failure(Exception("Failed to save device token")))
                            }
                        }
                } else {
                    emit(loginResult)
                }
            }
    }
}