package com.compose.data.repo

import android.util.Log
import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class MessagesRepositoryImpl(
    private val database: FirebaseDatabase
) : MessagesRepository {

    override suspend fun sendMessage(message: Message) : Flow<Result<Message>> = flow{
        try {
            val messageWithTimestamp=message.copy(timestamp = System.currentTimeMillis())
            val chatId = getChatId(message.senderId, message.receiverId)
            val messagesRef = database.reference.child("messages").child(chatId)
            messagesRef.push().setValue(messageWithTimestamp).await()
            emit(Result.success(messageWithTimestamp))

        } catch (e: Exception) {
            Log.e(TAG, "Failed to send message: ${e.localizedMessage}", e)
            emit(Result.failure(e))
        }
    }
    override fun getChatMessages(
        homeUserId: String,
        awayUserId: String
    ): Flow<Result<List<Message>>> = callbackFlow {
        val chatId = getChatId(homeUserId, awayUserId)
        val messagesRef = database.reference.child("messages").child(chatId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                trySend(Result.success(messages))
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to get chat messages: ${error.message}", error.toException())
                trySend(Result.failure(error.toException()))
                close(error.toException())
            }
        }
        messagesRef.addValueEventListener(listener)
        awaitClose { messagesRef.removeEventListener(listener) }
    }
    private fun getChatId(homeUserId: String, awayUserId: String): String {
        return if (homeUserId < awayUserId) "$homeUserId-$awayUserId" else "$awayUserId-$homeUserId"
    }
    companion object {
        const val TAG = "MessagesRepository"
    }
}
