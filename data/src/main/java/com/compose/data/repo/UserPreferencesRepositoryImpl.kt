package com.compose.data.repo
import com.compose.data.datasource.user.UserDataStoreManager
import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository

class UserPreferencesRepositoryImpl(private val userDataStoreManager: UserDataStoreManager) :
    UserPreferencesRepository {

    override suspend fun saveUserPreferences(user: User) {
        try {
            userDataStoreManager.cacheUser(user)
        } catch (e: Exception) {
           throw Exception("Failed to cache user: ${e.localizedMessage}")
        }
    }
    override suspend fun getCachedUser(): User {
        try {
          return userDataStoreManager.getUser()?: throw Exception("Failed to get logged in user")
        } catch (e: Exception) {
            throw Exception("Failed to get logged in user: ${e.localizedMessage}",e)
        }
    }
    override suspend fun clearUserPreferences() {
        try {
            // Perform the clear operation
            userDataStoreManager.clearUser()
        } catch (e: Exception) {
           throw Exception("Failed to clear user preferences: ${e.localizedMessage}", e)
        }
    }
}

