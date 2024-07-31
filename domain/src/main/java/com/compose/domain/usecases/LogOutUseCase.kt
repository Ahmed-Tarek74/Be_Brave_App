package com.compose.domain.usecases

import android.util.Log
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LogOutUseCase(
private val authRepository: AuthRepository,
private val userPreferencesRepository: UserPreferencesRepository
) {
    suspend operator fun invoke(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Perform logout operation
            authRepository.logOut()
            // Clear user preferences
            userPreferencesRepository.clearUserPreferences()
            // Return success result
            Result.success(Unit)
        } catch (e: Exception) {
            // Log the error
            Log.e(TAG, "Logout failed: ${e.localizedMessage}", e)
            // Return failure result
            Result.failure(e)
        }
    }
    companion object{
        const val TAG = "LogOutUseCase"
    }
}