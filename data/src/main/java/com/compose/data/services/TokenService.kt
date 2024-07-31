package com.compose.data.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class TokenService : ITokenService {
    override fun getToken(): Flow<String> = flow {
        val token = Firebase.messaging.token.await()
        emit(token)
    }
}
