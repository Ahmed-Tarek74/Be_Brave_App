package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetCachedUserUseCase(private val userPreferencesRepository: UserPreferencesRepository) {
    suspend operator fun invoke(): Flow<User?> =
        withContext(Dispatchers.IO) {
            userPreferencesRepository.getCachedUser()
        }
}