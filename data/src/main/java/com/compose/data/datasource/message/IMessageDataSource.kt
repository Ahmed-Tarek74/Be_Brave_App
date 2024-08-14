package com.compose.data.datasource.message

import com.compose.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface IMessageDataSource {
    suspend fun sendMessage(message: Message, chatId: String): Message
    fun getChatMessages(chatId: String): Flow<List<Message>>
}