package com.compose.data.datasource.deviceToken

interface IDeviceTokenDataSource {
    suspend fun saveDeviceToken(userId: String, token: String)
    suspend fun getDeviceToken(userId: String): String
}