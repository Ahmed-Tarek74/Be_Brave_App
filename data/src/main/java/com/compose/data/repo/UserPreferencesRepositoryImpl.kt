package com.compose.data.repo
import com.compose.data.datasource.user.IUserPreferencesDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository

class UserPreferencesRepositoryImpl(private val userPreferencesDataSource: IUserPreferencesDataSource) :
    UserPreferencesRepository {
    override suspend fun saveUserPreferences(user: User) {
        try {
            userPreferencesDataSource.cacheUser(user)
        } catch (e: Exception) {
           throw Exception("Failed to cache user: ${e.localizedMessage}")
        }
    }
    override suspend fun getCachedUser(): User {
        try {
          return userPreferencesDataSource.getUser()?: throw Exception("Failed to get logged in user")
        } catch (e: Exception) {
            throw Exception("Failed to get logged in user",e)
        }
    }
    override suspend fun clearUserPreferences() {
        try {
            // Perform the clear operation
            userPreferencesDataSource.clearUser()
        } catch (e: Exception) {
           throw Exception("Failed to clear user preferences: ${e.localizedMessage}", e)
        }
    }
}

