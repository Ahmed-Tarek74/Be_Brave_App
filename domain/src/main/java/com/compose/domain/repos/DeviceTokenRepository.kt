package com.compose.domain.repos

import kotlinx.coroutines.flow.Flow

interface DeviceTokenRepository {
    fun getAndSaveDeviceToken(userId: String): Flow<Boolean>
    fun getDeviceToken(userId: String): Flow<String?>
}