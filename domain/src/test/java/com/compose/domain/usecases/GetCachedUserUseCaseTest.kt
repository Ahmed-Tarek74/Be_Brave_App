package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetCachedUserUseCaseTest {

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var getCachedUserUseCase: GetCachedUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getCachedUserUseCase = GetCachedUserUseCase(userRepository)
    }
    @Test
    fun `invoke should return cached user`() = runTest(testDispatcher) {
        // Arrange
        val expectedUser = User(userId = "123", username = "Test User", email = "test@example.com")
        `when`(userRepository.getCachedUser()).thenReturn(expectedUser)

        // Act
        val actualUser = getCachedUserUseCase()

        // Assert
        assertEquals(expectedUser, actualUser)
        verify(userRepository, times(1)).getCachedUser()
    }
    @Test
    fun `invoke should return null when no user is cached`() = runTest(testDispatcher) {
        // Arrange
        val expected = null
        `when`(userRepository.getCachedUser()).thenReturn(null)

        // Act
        val actualUser = getCachedUserUseCase()

        // Assert
        assertEquals(expected, actualUser)
        verify(userRepository, times(1)).getCachedUser()
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when repository fails`() = runTest(testDispatcher) {
        // Arrange
        `when`(userRepository.getCachedUser()).thenThrow(Exception("Failed to get cached user"))

        // Act
        getCachedUserUseCase()

        // Assert
        verify(userRepository, times(1)).getCachedUser()
    }
}