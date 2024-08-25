package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.entities.User
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.RecentChatsRepository

class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    private val recentChatsRepository: RecentChatsRepository,
) {
    suspend operator fun invoke(message: Message, homeUser: User, awayUser: User) {
        val chatId = getChatId(message.senderId, message.receiverId)
        messagesRepository.sendMessage(message, chatId)
        updateRecentChats(message, homeUser, awayUser)
    }

    private suspend fun updateRecentChats(message: Message, homeUser: User, awayUser: User) {
        recentChatsRepository.updateRecentChats(message.receiverId, homeUser, message.message)
        recentChatsRepository.updateRecentChats(message.senderId, awayUser, message.message)
    }

    private fun getChatId(homeUserId: String, awayUserId: String): String {
        return if (homeUserId < awayUserId) "$homeUserId-$awayUserId" else "$awayUserId-$homeUserId"
    }
}
