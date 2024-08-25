package com.compose.data.repo

import com.compose.data.datasource.recentChat.RecentChatDataSource
import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import com.compose.domain.repos.RecentChatsRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class RecentChatsRepositoryImplTest {

    private lateinit var recentChatsDataSource: RecentChatDataSource
    private lateinit var recentChatsRepository: RecentChatsRepository

    @Before
    fun setUp() {
        recentChatsDataSource = mock()
        recentChatsRepository = RecentChatsRepositoryImpl(recentChatsDataSource)
    }

    @Test
    fun `getRecentChats should return flow of recent chats when successful`() = runTest {
        // Arrange
        val userId = "userId#1"
        val recentChats = listOf(RecentChat(User(username = "User 123"), "Last message"))
        val flowRecentChats: Flow<List<RecentChat>> = flowOf(recentChats)
        whenever(recentChatsDataSource.fetchRecentChats(userId)).thenReturn(flowRecentChats)

        // Act
        val result = recentChatsRepository.getRecentChats(userId)

        // Assert
        assertEquals(flowRecentChats, result)
        verify(recentChatsDataSource).fetchRecentChats(userId)
    }

    @Test
    fun `getRecentChats should throw exception when fetching fails`() = runTest {
        // Arrange
        val userId = "user123"
        whenever(recentChatsDataSource.fetchRecentChats(userId)).thenThrow(RuntimeException("Failed to fetch recent chats"))

        // Act & Assert
        val actualException = assertThrows(Exception::class.java) {
            recentChatsRepository.getRecentChats(userId)
        }
        val expectedExceptionMsg = "Failed to fetch recent chats for user: $userId"
        assertEquals(expectedExceptionMsg, actualException.message)
        verify(recentChatsDataSource).fetchRecentChats(userId)
    }

    @Test
    fun `updateRecentChats should complete without throwing exceptions when successful`() =
        runTest {
            // Arrange
            val homeUserId = "userId#123"
            val awayUser = User("userId#456", "Away User")
            val message = "Hello"
            whenever(
                recentChatsDataSource.updateRecentChat(
                    homeUserId,
                    awayUser,
                    message
                )
            ).thenReturn(Unit)

            // Act
            recentChatsRepository.updateRecentChats(homeUserId, awayUser, message)

            // Assert
            verify(recentChatsDataSource).updateRecentChat(homeUserId, awayUser, message)
        }

    @Test
    fun `updateRecentChats should throw exception when update fails`() = runTest {
        // Arrange
        val homeUserId = "homeUser123"
        val awayUser = User("awayUser123", "Away User")
        val message = "Hello"
        whenever(recentChatsDataSource.updateRecentChat(homeUserId, awayUser, message)).thenThrow(
            RuntimeException("Failed to update recent chat")
        )

        // Act & Assert
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                recentChatsRepository.updateRecentChats(homeUserId, awayUser, message)
            }
        }
        val expectedExceptionMsg = "Failed to update recent chats for user: $homeUserId"
        assertEquals(expectedExceptionMsg, actualException.message)
        verify(recentChatsDataSource).updateRecentChat(homeUserId, awayUser, message)
    }
}