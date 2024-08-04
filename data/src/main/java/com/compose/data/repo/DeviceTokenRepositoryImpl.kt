package com.compose.data.repo

import com.compose.data.services.ITokenService
import com.compose.domain.repos.DeviceTokenRepository
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DeviceTokenRepositoryImpl(
    private val tokenService: ITokenService,
    private val database: FirebaseDatabase
) : DeviceTokenRepository {

    override fun getAndSaveDeviceToken(userId: String): Flow<Boolean> = flow {
        tokenService.getToken().collect { token ->
            saveDeviceTokenToDatabase(userId, token)
            emit(true)
        }
    }.catch {
        emit(false)
    }

    override fun getDeviceToken(userId: String): Flow<String?> = flow {
        val tokenSnapshot = database.reference.child("user_tokens").child(userId).get().await()
        val token = tokenSnapshot.getValue(String::class.java)
        emit(token)
    }.catch {
        throw Exception("Failed to get device token")
    }

    private suspend fun saveDeviceTokenToDatabase(userId: String, token: String) {
        database.reference.child("user_tokens").child(userId).setValue(token).await()
    }
}
