package com.compose.domain.repos


interface DeviceTokenRepository {
    suspend fun saveDeviceToken(userId: String,token:String)
    suspend fun getDeviceToken():String
    suspend fun getDeviceToken(userId: String):String
}