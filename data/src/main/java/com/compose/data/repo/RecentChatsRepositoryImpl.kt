package com.compose.data.repo

import android.util.Log
import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import com.compose.domain.repos.RecentChatsRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RecentChatsRepositoryImpl(private val database: FirebaseDatabase) : RecentChatsRepository {

    override fun getRecentChats(userId: String): Flow<Result<List<RecentChat>>> = callbackFlow {
        val recentChatsRef = database.reference.child("recent_chats").child(userId)
        val orderedRecentChatsRef = recentChatsRef.orderByChild("timestamp")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val recentChats =
                        snapshot.children.mapNotNull { it.getValue(RecentChat::class.java) }
                    val reversedChats = recentChats.reversed()
                    trySend(Result.success(reversedChats))
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse recent chats: ${e.localizedMessage}", e)
                    trySend(Result.failure(e))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to get recent chats: ${error.message}", error.toException())
                trySend(Result.failure(error.toException()))
                close(error.toException())
            }
        }
        orderedRecentChatsRef.addValueEventListener(listener)
        awaitClose {
            orderedRecentChatsRef.removeEventListener(listener)
        }
    }

    override suspend fun updateRecentChats(
        homeUserId: String,
        awayUser: User,
        message: String
    ) {
        try {
            val recentChatRef =
                database.reference.child("recent_chats").child(homeUserId)
                    .child(awayUser.userId)
            val recentChat = RecentChat(
                awayUser = awayUser,
                recentMessage = message,
                timestamp = System.currentTimeMillis()
            )
            recentChatRef.setValue(recentChat).await()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update recent chats: ${e.localizedMessage}", e)
        }
    }

    companion object {
        const val TAG = "RecentChatsRepository"
    }
}