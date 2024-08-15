package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetUsersUseCaseTest {

    @Mock
    private lateinit var getUsersRepository: GetUsersRepository

    private lateinit var getUsersUseCase: GetUsersUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getUsersUseCase = GetUsersUseCase(getUsersRepository)
    }

    @Test
    fun `searchUsers should return list of users when called`() = runTest {
        // Arrange
        val query = "Tarek"
        val userId = "userId#123"
        val expectedUsers = listOf(
            User(userId = "userId#567", username = "Mo Tarek", email = "mo.tarek@example.com"),
            User(userId = "userId#345", username = "Ahmed Tarek", email = "ahmed@example.com")
        )

        `when`(getUsersRepository.searchUsers(query, userId)).thenReturn(expectedUsers)

        // Act
        val result = getUsersUseCase.searchUsers(query, userId)

        // Assert
        assertEquals(result, expectedUsers)
        verify(getUsersRepository, times(1)).searchUsers(query, userId)
    }

    @Test
    fun `searchUsers should return an empty list when no users match the query`() = runTest {
        // Arrange
        val query = "unknown"
        val userId = "user123"
        val expectedUsers = emptyList<User>()

        `when`(getUsersRepository.searchUsers(query, userId)).thenReturn(expectedUsers)

        // Act
        val result = getUsersUseCase.searchUsers(query, userId)

        // Assert
        assert(result.isEmpty())
        verify(getUsersRepository, times(1)).searchUsers(query, userId)
    }

    @Test(expected = Exception::class)
    fun `searchUsers should throw an exception when repository throws an exception`() = runTest {
        // Arrange
        val query = "john"
        val userId = "user123"

        `when`(getUsersRepository.searchUsers(query, userId)).thenThrow(Exception("Failed to search users"))

        // Act
        getUsersUseCase.searchUsers(query, userId)

        // Assert
        verify(getUsersRepository, times(1)).searchUsers(query, userId)
    }
}