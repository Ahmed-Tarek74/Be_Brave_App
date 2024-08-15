package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

open class GetRecentMessagesUseCase(private val messagesRepository: MessagesRepository) {
    suspend operator fun invoke(
        homeUserId: String,
        awayUserId: String
    ): Flow<List<Message>> =
        withContext(Dispatchers.IO) {
            val chatId = getChatId(homeUserId,awayUserId )
            messagesRepository.getChatMessages(chatId)
        }
    protected open fun getChatId(homeUserId: String, awayUserId: String): String {
        return if (homeUserId < awayUserId) "$homeUserId-$awayUserId" else "$awayUserId-$homeUserId"
    }
}