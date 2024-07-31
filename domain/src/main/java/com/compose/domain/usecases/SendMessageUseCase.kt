package com.compose.domain.usecases
import android.util.Log
import com.compose.domain.entities.Message
import com.compose.domain.entities.User
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.RecentChatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    private val recentChatsRepository: RecentChatsRepository,
) {
    suspend operator fun invoke(message: Message, homeUser: User, awayUser: User) {
        withContext(Dispatchers.IO) {
            try {
                messagesRepository.sendMessage(message).collect { sendResult ->
                    if (sendResult.isSuccess) {
                        // Update recent chats
                        updateRecentChats(message, homeUser, awayUser)
                    } else {
                        Log.e(TAG, "Failed to send message: ${sendResult.exceptionOrNull()?.message}")
                        throw Exception("Failed to send message")
                    }
                }
            } catch (e: Exception) {
                // Log the error and return failure result
                Log.e(TAG, "Error in SendMessageUseCase: ${e.message}", e)
                throw Exception("Failed to send message")
            }
        }
    }
    private suspend fun updateRecentChats(message: Message, homeUser: User, awayUser: User) {
        try {
            recentChatsRepository.updateRecentChats(message.receiverId, homeUser, message.message)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update recent chats for receiver: ${e.message}", e)
        }
        try {
            recentChatsRepository.updateRecentChats(message.senderId, awayUser, message.message)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to update recent chats for sender: ${e.message}", e)
        }
    }
    companion object {
        const val TAG = "SendMessageUseCase"
    }
}
