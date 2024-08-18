package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.entities.User
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.RecentChatsRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class SendMessageUseCaseTest {

    @Mock
    private lateinit var messagesRepository: MessagesRepository

    @Mock
    private lateinit var recentChatsRepository: RecentChatsRepository

    private lateinit var sendMessageUseCase: SendMessageUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        sendMessageUseCase = SendMessageUseCase(messagesRepository, recentChatsRepository)
    }

    @Test
    fun `invoke should send message and update recent chats`() = runTest {
        // Arrange
        val message = Message(
            senderId = "userId#123",
            receiverId = "userId#456",
            message = "Hello"
        )
        val homeUser = User(userId = "userId#123", username = "Mo Tareq")
        val awayUser = User(userId = "userId#456", username = "Mahmoud Khaled")
        val chatId = "userId#123-userId#456"
        // Act
        sendMessageUseCase(message, homeUser, awayUser)

        // Assert
        verify(messagesRepository, times(1)).sendMessage(message, chatId)
        verify(recentChatsRepository, times(1)).updateRecentChats(
            message.receiverId,
            homeUser,
            message.message
        )
        verify(recentChatsRepository, times(1)).updateRecentChats(
            message.senderId,
            awayUser,
            message.message
        )
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when sending message fails`() = runTest {
        // Arrange
        val message = Message(senderId = "userId#123", receiverId = "userId#456", message = "Hello")
        val homeUser = User(userId = "userId#123", username = "Mo Tareq")
        val awayUser = User(userId = "userId#456", username = "Mahmoud Khaled")
        val chatId = "userId#123-userId#456"
        `when`(
            messagesRepository.sendMessage(
                message,
                chatId
            )
        ).thenThrow(Exception("Failed to send message"))

        // Act
        sendMessageUseCase(message, homeUser, awayUser)

        // Assert
        verify(messagesRepository, times(1)).sendMessage(message, chatId)
        verify(recentChatsRepository, never()).updateRecentChats(anyString(), any(), anyString())
    }
}