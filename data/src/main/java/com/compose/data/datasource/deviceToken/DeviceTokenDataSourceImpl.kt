package com.compose.data.datasource.deviceToken

import com.compose.data.services.ITokenService
import com.compose.domain.utils.EventLogger
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DeviceTokenDataSourceImpl(
    private val database: FirebaseDatabase,
    private val tokenService: ITokenService,
    private val eventLogger: EventLogger
) : DeviceTokenDataSource {

    override suspend fun setDeviceTokenToUser(userId: String) {
        try {
            eventLogger.logEvent("AttemptToGetDeviceToken", mapOf("userId" to userId))
            val token =tokenService.getToken()
            eventLogger.logEvent("AttemptToSaveDeviceToken", mapOf("userId" to userId))
            database.reference.child("user_tokens").child(userId).setValue(token).await()
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to save device token for user: $userId", e)
        }
    }

    override suspend fun getDeviceToken(userId: String): String {
        return try {
            eventLogger.logEvent("AttemptToRetrieveDeviceToken", mapOf("userId" to userId))
            val tokenSnapshot = database.reference.child("user_tokens").child(userId).get().await()
            tokenSnapshot.getValue(String::class.java)
                ?: throw DeviceTokenException("Device token is null for user: $userId")
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to retrieve device token for user: $userId", e)
        }
    }
}
