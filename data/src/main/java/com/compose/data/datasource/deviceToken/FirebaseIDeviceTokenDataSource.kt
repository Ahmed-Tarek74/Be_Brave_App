package com.compose.data.datasource.deviceToken

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseIDeviceTokenDataSource(
    private val database: FirebaseDatabase
) : IDeviceTokenDataSource {

    override suspend fun saveDeviceToken(userId: String, token: String) {
        try {
            database.reference.child("user_tokens").child(userId).setValue(token).await()
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to save device token for user: $userId", e)
        }
    }

    override suspend fun getDeviceToken(userId: String): String {
        return try {
            val tokenSnapshot = database.reference.child("user_tokens").child(userId).get().await()
            tokenSnapshot.getValue(String::class.java)
                ?: throw DeviceTokenException("Device token is null for user: $userId")
        } catch (e: Exception) {
            throw DeviceTokenException("Failed to retrieve device token for user: $userId", e)
        }
    }
}
class DeviceTokenException(message: String, cause: Throwable? = null) : Exception(message, cause)
