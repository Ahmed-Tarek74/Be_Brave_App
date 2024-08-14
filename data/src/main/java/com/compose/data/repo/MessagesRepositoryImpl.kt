package com.compose.data.repo

import com.compose.data.datasource.message.IMessageDataSource
import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import kotlinx.coroutines.flow.Flow

class MessagesRepositoryImpl(
    private val messagesDataSource: IMessageDataSource
) : MessagesRepository {

    override suspend fun sendMessage(message: Message, chatId: String): Message {
        return try {
            messagesDataSource.sendMessage(message, chatId)
        } catch (e: Exception) {
            throw Exception("Failed to send message to chat with ID: $chatId", e)
        }
    }

    override fun getChatMessages(chatId: String): Flow<List<Message>> {
        return try {
            messagesDataSource.getChatMessages(chatId)
        } catch (e: Exception) {
            throw Exception("Failed to retrieve messages for chat with ID: $chatId", e)
        }
    }
}