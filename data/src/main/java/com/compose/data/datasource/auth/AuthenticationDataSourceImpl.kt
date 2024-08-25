package com.compose.data.datasource.auth

import com.compose.domain.utils.EventLogger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthenticationDataSourceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val eventLogger: EventLogger
) : AuthenticationDataSource {
    override suspend fun login(email: String, password: String): String {
        eventLogger.logEvent("AttemptToLogin")
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return authResult.user?.uid
            ?: throw Exception("Failed to get user ID from authentication result.")
    }

    override suspend fun register(email: String, password: String): String {
        eventLogger.logEvent("AttemptToRegister")
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return authResult.user?.uid
            ?: throw Exception("Failed to get user ID from authentication result.")
    }

    override fun logout() {
        eventLogger.logEvent("AttemptToLogin")
        firebaseAuth.signOut()
    }
}