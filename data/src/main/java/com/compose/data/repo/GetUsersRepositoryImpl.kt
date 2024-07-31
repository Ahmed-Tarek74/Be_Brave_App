package com.compose.data.repo

import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GetUsersRepositoryImpl(private val database: FirebaseDatabase) : GetUsersRepository {
    override suspend fun searchUsers(searchQuery: String, homeUserId: String): Flow<List<User>> =
        callbackFlow {
            val usersRef = database.reference.child("users")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                        .filter {
                            (it.username.contains(searchQuery, ignoreCase = true))
                                    && (it.userId != homeUserId)
                        }
                    trySend(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            }
            usersRef.addValueEventListener(listener)
            awaitClose { usersRef.removeEventListener(listener) }
        }
    override fun getUserById(userId: String): Flow<User?> = callbackFlow {

        val usersRef = database.reference.child("users").child(userId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                trySend(user)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        usersRef.addValueEventListener(listener)
        awaitClose { usersRef.removeEventListener(listener) }
    }

}


