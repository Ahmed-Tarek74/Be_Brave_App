package com.compose.domain.repos


interface DeviceTokenRepository {
    suspend fun getAndSaveDeviceToken(userId: String)
    suspend fun getDeviceToken(userId: String):String
}