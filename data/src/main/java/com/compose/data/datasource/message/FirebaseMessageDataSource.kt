package com.compose.data.datasource.message

import com.compose.domain.entities.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseMessageDataSource(private val database: FirebaseDatabase) : IMessageDataSource {


    override suspend fun sendMessage(message: Message, chatId: String): Message {
        val messageWithTimestamp = message.copy(timestamp = System.currentTimeMillis())
        return try {
            val messagesRef = database.reference.child("messages").child(chatId)
            messagesRef.push().setValue(messageWithTimestamp).await()
            messageWithTimestamp
        } catch (e: Exception) {
            throw Exception("Failed to send message to chat: $chatId", e)
        }
    }

    override fun getChatMessages(chatId: String): Flow<List<Message>> = callbackFlow {
        val messagesRef = database.reference.child("messages").child(chatId)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull { it.getValue(Message::class.java) }
                trySend(messages)
            }
            override fun onCancelled(error: DatabaseError) {
                close(Exception("Failed to fetch messages for chat: $chatId", error.toException()))
            }
        }
        messagesRef.addValueEventListener(listener)
        awaitClose { messagesRef.removeEventListener(listener) }
    }
}

