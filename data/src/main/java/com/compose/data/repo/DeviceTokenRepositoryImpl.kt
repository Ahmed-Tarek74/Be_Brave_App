package com.compose.data.repo

import com.compose.data.services.ITokenService
import com.compose.domain.repos.DeviceTokenRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DeviceTokenRepositoryImpl(
    private val tokenService: ITokenService,
    private val database: FirebaseDatabase
) : DeviceTokenRepository {

    override fun getAndSaveDeviceToken(userId: String): Flow<Boolean> = flow {
        try {
            tokenService.getToken().collect { token ->
                saveDeviceTokenToDatabase(userId, token)
                emit(true)
            }
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun getDeviceToken(userId: String): Flow<String?> = flow {
        try {
            val tokenSnapshot = database.reference.child("user_tokens").child(userId).get().await()
            emit(tokenSnapshot.getValue(String::class.java))
        } catch (e: Exception) {
            emit(null)
        }
    }

    private suspend fun saveDeviceTokenToDatabase(userId: String, token: String) {
        database.reference.child("user_tokens").child(userId).setValue(token).await()
    }
}
