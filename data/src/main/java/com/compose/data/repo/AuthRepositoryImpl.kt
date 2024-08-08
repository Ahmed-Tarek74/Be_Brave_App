package com.compose.data.repo

import android.util.Log
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseDatabase
) : AuthRepository {

    override suspend fun login(email: String, password: String): User{
        try {
            // Sign in with email and password
            val authResult =
                firebaseAuth.signInWithEmailAndPassword(email,password).await()
            val userId = authResult.user?.uid ?: throw Exception("User ID is null. Login failed.")

            // Fetch user data from the database
            val loggedInUser = withContext(Dispatchers.IO) { fetchUserFromDatabase(userId) }
            // return success result
            return loggedInUser
        } catch (e: Exception) {
            Log.e(TAG, "Login failed: ${e.localizedMessage}", e)
            throw e
        }
    }
    override fun register(user: User): Flow<Result<User>> = flow {
        try {
            val authResult =
                firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()
            val uid =
                authResult.user?.uid ?: throw Exception("User ID is null. Registration failed.")
            val registeredUser = user.copy(userId = uid)

            // Save user data to Realtime Database
            saveUserToDatabase(registeredUser)

            emit(Result.success(registeredUser))
        } catch (e: Exception) {
            Log.e(TAG, "Registration failed: ${e.localizedMessage}", e)
            emit(Result.failure(e))
        }
    }

    override suspend fun logOut() {
        try {
            firebaseAuth.signOut()
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed: ${e.localizedMessage}", e)
        }
    }

    private suspend fun fetchUserFromDatabase(userId: String): User {
        val dataSnapshot = database.reference.child("users").child(userId).get().await()
        return dataSnapshot.getValue(User::class.java)
            ?: throw Exception("Failed to retrieve user data from database.")
    }

    private suspend fun saveUserToDatabase(user: User) {
        val userRef = database.reference.child("users").child(user.userId)
        userRef.setValue(user).await()
    }

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }
}
