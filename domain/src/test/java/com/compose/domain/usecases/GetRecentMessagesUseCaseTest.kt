package com.compose.domain.usecases

import com.compose.domain.entities.Message
import com.compose.domain.repos.MessagesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetRecentMessagesUseCaseTest {
    @Mock
    private lateinit var messagesRepository: MessagesRepository

    private lateinit var getRecentMessagesUseCase: GetRecentMessagesUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getRecentMessagesUseCase = GetRecentMessagesUseCase(messagesRepository)
    }

    @Test
    fun `invoke should return chat messages when called`() = runTest {
        // Arrange
        val homeUserId = "userId#123"
        val awayUserId = "userId#456"
        val chatId = "userId#123-userId#456"

        val messages = listOf(
            Message(
                senderId = "userId#123",
                receiverId = "userId#456",
                message = "Hello",
                timestamp = 1625247600000
            ),
            Message(
                senderId = "userId#123",
                receiverId = "userId#456",
                message = "Hello",
                timestamp = 1625247600000
            )
        )
        val flowMessages: Flow<List<Message>> = flowOf(messages)

        `when`(messagesRepository.getChatMessages(chatId)).thenReturn(flowMessages)

        // Act
        val result = getRecentMessagesUseCase(homeUserId, awayUserId)

        // Assert
        assert(result == flowMessages)
        verify(messagesRepository, times(1)).getChatMessages(chatId)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Arrange
        val homeUserId = "userId#123"
        val awayUserId = "userId#456"
        val chatId = "userId#123-userId#456"

        `when`(messagesRepository.getChatMessages(chatId)).thenThrow(Exception("Failed to fetch messages"))

        // Act
        getRecentMessagesUseCase(homeUserId, awayUserId)

        // Assert
        verify(messagesRepository, times(1)).getChatMessages(chatId)
    }

    @Test
    fun `getChatId should return correct chat ID when homeUserId is less than awayUserId`() {
        // Arrange
        val homeUserId = "a123"
        val awayUserId = "b456"
        val expectedChatId = "a123-b456"

        // Act
        val actualId = GetRecentMessagesUseCaseTestable().getChatId(homeUserId, awayUserId)

        // Assert
        assertEquals(actualId, expectedChatId)
    }

    @Test
    fun `getChatId should return correct chat ID when homeUserId is greater than awayUserId`() {
        // Arrange
        val homeUserId = "b456"
        val awayUserId = "a123"
        val expectedChatId = "a123-b456"

        // Act
        val actualId = GetRecentMessagesUseCaseTestable().getChatId(homeUserId, awayUserId)

        // Assert
        assertEquals(actualId, expectedChatId)
    }

    // Helper class to access private methods for testing purposes
    private inner class GetRecentMessagesUseCaseTestable :
        GetRecentMessagesUseCase(messagesRepository) {
          public override fun getChatId(homeUserId: String, awayUserId: String): String {
            return super.getChatId(homeUserId, awayUserId)
        }
    }
}