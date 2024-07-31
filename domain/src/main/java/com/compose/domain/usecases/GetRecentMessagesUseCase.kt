package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRecentMessagesUseCase(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(homeUserId:String, awayUserId:String): Flow<Result<List<Message>>> =
        withContext(Dispatchers.IO) {
            messagesRepository.getChatMessages(homeUserId, awayUserId)
        }
}