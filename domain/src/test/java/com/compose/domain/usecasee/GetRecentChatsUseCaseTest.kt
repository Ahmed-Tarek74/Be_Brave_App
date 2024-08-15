package com.compose.domain.usecasee

import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import com.compose.domain.repos.RecentChatsRepository
import com.compose.domain.usecases.GetRecentChatsUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetRecentChatsUseCaseTest {

    @Mock
    private lateinit var recentChatsRepository: RecentChatsRepository

    private lateinit var getRecentChatsUseCase: GetRecentChatsUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getRecentChatsUseCase = GetRecentChatsUseCase(recentChatsRepository)
    }

    @Test
    fun `invoke should return flow of recent chats`() = runTest(testDispatcher) {
        // Arrange
        val userId = "user123"
        val expectedChats = listOf(
            RecentChat(awayUser = User(username = "user one"), recentMessage  = "Hello", timestamp = 1625247600000),
            RecentChat(awayUser = User(username = "user two"), recentMessage = "Hey", timestamp = 1625251200000)
        )
        val expectedFlow: Flow<List<RecentChat>> = flowOf(expectedChats)

        `when`(recentChatsRepository.getRecentChats(userId)).thenReturn(expectedFlow)

        // Act
        val actualFlow = getRecentChatsUseCase(userId)

        // Assert
        actualFlow.collect { actualChats ->
            assertEquals(expectedChats, actualChats)
        }
        verify(recentChatsRepository, times(1)).getRecentChats(userId)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when repository fails`() = runTest(testDispatcher) {
        // Arrange
        val userId = "user123"
        `when`(recentChatsRepository.getRecentChats(userId)).thenThrow(Exception("Failed to fetch recent chats"))
        // Act
        getRecentChatsUseCase(userId)
        // Assert
        verify(recentChatsRepository, times(1)).getRecentChats(userId)
    }
}