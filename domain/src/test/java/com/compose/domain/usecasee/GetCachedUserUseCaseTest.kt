package com.compose.domain.usecasee

import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository
import com.compose.domain.usecases.GetCachedUserUseCase
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
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var getCachedUserUseCase: GetCachedUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getCachedUserUseCase = GetCachedUserUseCase(userPreferencesRepository)
    }
    @Test
    fun `invoke should return cached user`() = runTest(testDispatcher) {
        // Arrange
        val expectedUser = User(userId = "123", username = "Test User", email = "test@example.com")
        `when`(userPreferencesRepository.getCachedUser()).thenReturn(expectedUser)

        // Act
        val actualUser = getCachedUserUseCase()

        // Assert
        assertEquals(expectedUser, actualUser)
        verify(userPreferencesRepository, times(1)).getCachedUser()
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when repository fails`() = runTest(testDispatcher) {
        // Arrange
        `when`(userPreferencesRepository.getCachedUser()).thenThrow(Exception("Failed to get cached user"))

        // Act
        getCachedUserUseCase()

        // Assert
        verify(userPreferencesRepository, times(1)).getCachedUser()
    }
}