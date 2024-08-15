package com.compose.data.datasource.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FirebaseAuthentication(private val firebaseAuth: FirebaseAuth): IAuthentication
{
     override suspend fun login(email: String, password: String): String {
        val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return authResult.user?.uid ?: throw Exception("Failed to get user ID from authentication result.")
    }
     override suspend fun register(email: String, password: String): String {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        return authResult.user?.uid ?: throw Exception("Failed to get user ID from authentication result.")
    }
     override fun logout() {
        firebaseAuth.signOut()
    }
}