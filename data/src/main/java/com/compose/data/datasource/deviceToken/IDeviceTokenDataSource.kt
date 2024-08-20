package com.compose.data.datasource.deviceToken

interface IDeviceTokenDataSource {
    suspend fun setDeviceTokenToUser(userId: String)
    suspend fun getDeviceToken(userId: String): String
}