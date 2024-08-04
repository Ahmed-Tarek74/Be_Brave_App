package com.compose.domain.repos

import com.compose.domain.entities.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun sendMessage(message: Message,chatId:String): Flow<Result<Message>>
    fun getChatMessages(chatId:String): Flow<Result<List<Message>>>
}