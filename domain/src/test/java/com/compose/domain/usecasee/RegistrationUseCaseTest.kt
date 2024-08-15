package com.compose.domain.usecasee

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.usecases.RegistrationUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class RegistrationUseCaseTest {
    @Mock
    private lateinit var authRepository: AuthRepository

    private lateinit var registrationUseCase: RegistrationUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registrationUseCase = RegistrationUseCase(authRepository)
    }

    @Test
    fun `invoke should register user when passwords match`() = runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "password123")
        val confirmationPassword = "password123"
        `when`(authRepository.register(user)).thenReturn(user)

        // Act
        val registeredUser = registrationUseCase(user, confirmationPassword)

        // Assert
        assertEquals(user, registeredUser)
        verify(authRepository, times(1)).register(user)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when passwords do not match`() = runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "password123")
        val confirmationPassword = "differentPassword"

        // Act
        registrationUseCase(user, confirmationPassword)

        // Assert (Exception is expected)
        verify(authRepository, never()).register(user)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when repository throws exception`() = runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "password123")
        val confirmationPassword = "password123"
        `when`(authRepository.register(user)).thenThrow(Exception("Registration failed"))

        // Act
        registrationUseCase(user, confirmationPassword)

        // Assert (Exception is expected)
        verify(authRepository, times(1)).register(user)
    }
}