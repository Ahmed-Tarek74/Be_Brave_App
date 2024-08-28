package com.compose.data.datasource.user

import com.compose.domain.entities.User
import com.compose.domain.utils.EventLogger
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val remoteUserDataManager: RemoteUserDataManager,
    private val userPreferencesManager: UserPreferencesManager,
    private val eventLogger: EventLogger
) : UserDataSource {
    override suspend fun getUserById(userId: String): User? =
        remoteUserDataManager.getUserById(userId)

    override suspend fun getUsersByUsername(searchQuery: String): List<User> =
        remoteUserDataManager.getUsersByUsername(searchQuery)

    override suspend fun addUser(user: User) = remoteUserDataManager.addUser(user)

    override suspend fun cacheUser(user: User) = userPreferencesManager.cacheUser(user)

    override suspend fun clearUser() = userPreferencesManager.clearUser()

    override suspend fun getCachedUser(): User? = userPreferencesManager.getCachedUser()
    override fun logEvent(eventName: String, params: Map<String, String>) {
        eventLogger.logEvent(eventName, params)
    }
}