package com.compose.data.datasource.deviceToken

interface DeviceTokenDataSource {
    suspend fun setDeviceTokenToUser(userId: String)
    suspend fun getDeviceToken(userId: String): String
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())

}