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
import com.google.firebase.database.Query

class GetUsersRepositoryImpl(private val database: FirebaseDatabase) : GetUsersRepository {

    override fun searchUsers(searchQuery: String, homeUserId: String): Flow<List<User>> = callbackFlow {
        val usersRef = database.reference.child("users")
        // Firebase query to filter users based on the search query
        val query: Query = usersRef.orderByChild("username").startAt(searchQuery).endAt(searchQuery + "\uf8ff")

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = snapshot.children.mapNotNull { it.getValue(User::class.java) }
                    .filter { it.userId != homeUserId }  // Additional client-side filter to exclude current user
                trySend(users)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        query.addValueEventListener(listener)
        awaitClose { query.removeEventListener(listener) }
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


