package com.compose.data.datasource.recentChat

import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RecentChatDataSourceImpl(private val database: FirebaseDatabase) :
    RecentChatDataSource {

    override fun fetchRecentChats(userId: String): Flow<List<RecentChat>> = callbackFlow {
        val recentChatsRef = database.reference.child("recent_chats").child(userId)
        val orderedRecentChatsRef = recentChatsRef.orderByChild("timestamp")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val recentChats =
                        snapshot.children.mapNotNull { it.getValue(RecentChat::class.java) }
                    val reversedChats = recentChats.reversed()
                    trySend(reversedChats)
                } catch (e: Exception) {
                    throw Exception("Error processing recent chats for user: $userId", e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                val exception = Exception("Database error: ${error.message}", error.toException())
                close(error.toException())
                throw exception
            }
        }
        orderedRecentChatsRef.addValueEventListener(listener)
        awaitClose { orderedRecentChatsRef.removeEventListener(listener) }
    }

    override suspend fun updateRecentChat(homeUserId: String, awayUser: User, message: String) {
        val recentChatRef =
            database.reference.child("recent_chats").child(homeUserId).child(awayUser.userId)
        val recentChat = RecentChat(
            awayUser = awayUser,
            recentMessage = message,
            timestamp = System.currentTimeMillis()
        )
        try {
            recentChatRef.setValue(recentChat).await()
        } catch (e: Exception) {
            throw Exception("Failed to update recent chats: ${e.localizedMessage}", e)
        }
    }
}