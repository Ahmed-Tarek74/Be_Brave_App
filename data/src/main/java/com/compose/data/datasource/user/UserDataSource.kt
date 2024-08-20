package com.compose.data.datasource.user

import com.compose.domain.entities.User
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val remoteUserDataManager: IRemoteUserDataManager,
    private val userPreferencesManager: IUserPreferencesManager
) : IUserDataSource {
    override suspend fun getUserById(userId: String): User? =
        remoteUserDataManager.getUserById(userId)

    override suspend fun getUsersByUsername(searchQuery: String): List<User> =
        remoteUserDataManager.getUsersByUsername(searchQuery)

    override suspend fun addUser(user: User) = remoteUserDataManager.addUser(user)

    override suspend fun cacheUser(user: User) = userPreferencesManager.cacheUser(user)

    override suspend fun clearUser() = userPreferencesManager.clearUser()

    override suspend fun getCachedUser(): User? = userPreferencesManager.getCachedUser()
}