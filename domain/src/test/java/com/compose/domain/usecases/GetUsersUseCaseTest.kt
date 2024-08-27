package com.compose.domain.usecases

import com.compose.domain.base.BaseUseCaseTest
import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetUsersUseCaseTest :BaseUseCaseTest(){

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getUsersUseCase = GetUsersUseCase(userRepository)
    }

    @Test
    fun `searchUsers should return list of users when called`() = runTest(testDispatcher) {
        // Arrange
        val query = "Tarek"
        val userId = "userId#123"
        val expectedUsers = listOf(
            User(userId = "userId#567", username = "Mo Tarek", email = "mo.tarek@example.com"),
            User(userId = "userId#345", username = "Ahmed Tarek", email = "ahmed@example.com")
        )

        `when`(userRepository.searchUsers(query, userId)).thenReturn(expectedUsers)

        // Act
        val result = getUsersUseCase(query, userId)

        // Assert
        assertEquals(result, expectedUsers)
        verify(userRepository, times(1)).searchUsers(query, userId)
    }

    @Test
    fun `searchUsers should return an empty list when no users match the query`() = runTest {
        // Arrange
        val query = "unknown"
        val userId = "user123"
        val expectedUsers = emptyList<User>()

        `when`(userRepository.searchUsers(query, userId)).thenReturn(expectedUsers)

        // Act
        val result = getUsersUseCase(query, userId)

        // Assert
        assert(result.isEmpty())
        verify(userRepository, times(1)).searchUsers(query, userId)
    }

    @Test(expected = Exception::class)
    fun `searchUsers should throw an exception when repository throws an exception`() = runTest(testDispatcher) {
        // Arrange
        val query = "john"
        val userId = "user123"

        `when`(
            userRepository.searchUsers(
                query,
                userId
            )
        ).thenThrow(Exception("Failed to search users"))

        // Act
        getUsersUseCase(query, userId)

        // Assert
        verify(userRepository, times(1)).searchUsers(query, userId)
    }
}