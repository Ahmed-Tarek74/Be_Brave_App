package com.compose.data.repo

import com.compose.data.datasource.deviceToken.DeviceTokenDataSource
import com.compose.data.datasource.deviceToken.DeviceTokenException
import com.compose.domain.repos.DeviceTokenRepository

class DeviceTokenRepositoryImpl(
    private val deviceTokenDataSource: DeviceTokenDataSource
) : DeviceTokenRepository {

    override suspend fun setDeviceTokenToUser(userId: String) {
        try {
            deviceTokenDataSource.setDeviceTokenToUser(userId)
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to save device token for user: $userId", e)
        }
    }
    override suspend fun getDeviceToken(userId: String): String {
        return try {
            deviceTokenDataSource.getDeviceToken(userId)
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to retrieve device token for user: $userId", e)
        }
    }

}