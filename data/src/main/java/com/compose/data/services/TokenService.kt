package com.compose.data.services

import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

class TokenService : ITokenService {

    override suspend fun getToken(): String {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            throw TokenRetrievalException("Failed to retrieve FCM device token", e)
        }
    }
}
class TokenRetrievalException(message: String, cause: Throwable) : Exception(message, cause)
