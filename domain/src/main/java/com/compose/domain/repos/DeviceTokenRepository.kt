package com.compose.domain.repos


interface DeviceTokenRepository {
    suspend fun setDeviceTokenToUser(userId: String)
    suspend fun getDeviceToken(userId: String):String
}