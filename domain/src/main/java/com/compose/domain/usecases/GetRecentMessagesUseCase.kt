package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import kotlinx.coroutines.flow.Flow

open class GetRecentMessagesUseCase(private val messagesRepository: MessagesRepository) {
    operator fun invoke(
        homeUserId: String,
        awayUserId: String
    ): Flow<List<Message>> {
        val chatId = getChatId(homeUserId, awayUserId)
        return messagesRepository.getChatMessages(chatId)
    }

    protected open fun getChatId(homeUserId: String, awayUserId: String): String {
        return if (homeUserId < awayUserId) "$homeUserId-$awayUserId" else "$awayUserId-$homeUserId"
    }
}