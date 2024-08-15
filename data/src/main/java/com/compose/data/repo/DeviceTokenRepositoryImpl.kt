package com.compose.data.repo

import com.compose.data.datasource.deviceToken.IDeviceTokenDataSource
import com.compose.data.datasource.deviceToken.DeviceTokenException
import com.compose.data.services.ITokenService
import com.compose.domain.repos.DeviceTokenRepository

class DeviceTokenRepositoryImpl(
    private val tokenService: ITokenService,
    private val deviceTokenDataSource: IDeviceTokenDataSource
) : DeviceTokenRepository {

    override suspend fun saveDeviceToken(userId: String, token: String) {
        try {
            deviceTokenDataSource.saveDeviceToken(userId,token)
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to save device token for user: $userId", e)
        }
    }
    override suspend fun getDeviceToken(): String {
        return try {
            tokenService.getToken()
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to get device token", e)
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