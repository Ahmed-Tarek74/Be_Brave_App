package com.compose.data.datasource.user

import com.compose.domain.entities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseUserDataSource(
    private val database: FirebaseDatabase
): IFirebaseUserDataSource {
    override suspend fun getUserById(userId: String): User? {
        val dataSnapshot = database.reference.child("users").child(userId).get().await()
        return dataSnapshot.getValue(User::class.java)
    }
    override suspend fun getUsersByUsername(searchQuery: String): List<User> {
        val usersRef = database.reference.child("users")
        val query: Query =
            usersRef.orderByChild("username").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                    if (continuation.isActive) {
                        continuation.resume(users)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWithException(error.toException())
                    }
                }
            }
            query.addValueEventListener(listener)
            continuation.invokeOnCancellation {
                query.removeEventListener(listener)
            }
        }
    }

    override suspend fun saveUser(user: User) {
        val userRef = database.reference.child("users").child(user.userId)
        userRef.setValue(user).await()
    }
}
