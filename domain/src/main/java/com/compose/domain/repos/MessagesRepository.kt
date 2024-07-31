package com.compose.domain.repos

import com.compose.domain.entities.Message
import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun sendMessage(message: Message): Flow<Result<Message>>
    fun getChatMessages(homeUserId:String,awayUserId:String): Flow<Result<List<Message>>>
}