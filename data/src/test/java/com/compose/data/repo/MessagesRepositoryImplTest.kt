package com.compose.data.repo

import com.compose.data.datasource.message.MessageDataSource
import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class MessagesRepositoryImplTest {
    private lateinit var  messagesDataSource: MessageDataSource
    private lateinit var messagesRepository : MessagesRepository

    @Before
    fun setUp(){
        messagesDataSource=mock()
        messagesRepository=MessagesRepositoryImpl(messagesDataSource)
    }
    @Test
    fun `sendMessage should return message when successful`() = runTest {
        // Arrange
        val message = Message("senderId", "receiverId", "Hello")
        val chatId = "chatId#123"
        whenever(messagesDataSource.sendMessage(message, chatId)).thenReturn(message)
        val actual = messagesRepository.sendMessage(message, chatId)
        // Assert
        assertEquals(message, actual)
    }

    @Test
    fun `sendMessage should throw exception when sending fails`() = runTest {
        // Arrange
        val message = Message("senderId", "receiverId", "Hello")
        val chatId = "chatId#123"
        whenever(messagesDataSource.sendMessage(message, chatId)).thenThrow(RuntimeException("Fail to send message"))
        //Act
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                messagesRepository.sendMessage(message, chatId)
            }
        }
        val expectedExceptionMsg = "Failed to send message to chat with ID: $chatId"
        //Assert
        assertEquals(expectedExceptionMsg, actualException.message)
    }

    @Test
    fun `getChatMessages should return flow of messages when successful`() = runTest {
        // Arrange
        val chatId = "chat123"
        val messages = listOf(Message("senderId", "receiverId", "Hello"))
        val flowMessages: Flow<List<Message>> = flowOf(messages)
        whenever(messagesDataSource.getChatMessages(chatId)).thenReturn(flowMessages)

        // Act
        val result = messagesRepository.getChatMessages(chatId)
        // Assert
        assertEquals(flowMessages, result)
    }

    @Test
    fun `getChatMessages should throw exception when retrieval fails`() = runTest {
        // Arrange
        val chatId = "chat123"
        whenever(messagesDataSource.getChatMessages(chatId)).thenThrow(RuntimeException("Retrieval failed"))
        // Act & Assert
        val actualException = assertThrows(Exception::class.java) {
            messagesRepository.getChatMessages(chatId)
        }
        val expectedExceptionMsg="Failed to retrieve messages for chat with ID: $chatId"
        assertEquals(expectedExceptionMsg, actualException.message)
    }
}