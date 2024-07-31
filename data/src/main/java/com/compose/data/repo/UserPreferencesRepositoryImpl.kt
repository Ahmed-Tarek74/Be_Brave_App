package com.compose.data.repo

import android.util.Log
import com.compose.data.local.DataStoreManager
import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserPreferencesRepositoryImpl(private val dataStoreManager: DataStoreManager) :
    UserPreferencesRepository {

    override suspend fun saveUserPreferences(user: User) {
        try {
            dataStoreManager.cacheUser(user)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to cache user: ${e.localizedMessage}")
        }
    }

    override fun getCachedUser(): Flow<User?> = flow {
        try {
            emit(dataStoreManager.getUser())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get logged in user: ${e.localizedMessage}", e)
            emit(null)
        }
    }


    override suspend fun clearUserPreferences() {
        try {
            // Perform the clear operation
            dataStoreManager.clearUser()
            // Log success
            Log.d(TAG, "User preferences cleared successfully.")

        } catch (e: Exception) {
            // Log the error
            Log.e(TAG, "Failed to clear user preferences: ${e.localizedMessage}", e)
        }
    }

    companion object {
        private const val TAG = "UserCacheRepositoryImpl"
    }

}

