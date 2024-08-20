package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class RegistrationUseCaseTest {
    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var registrationUseCase: RegistrationUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        registrationUseCase = RegistrationUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke should register user and add user to repository when passwords match`() = runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "password123")
        val confirmationPassword = "password123"
        `when`(authRepository.register(user)).thenReturn(user)

        // Act
        val registeredUser = registrationUseCase(user, confirmationPassword)

        // Assert
        assertEquals(user, registeredUser)
        verify(authRepository, times(1)).register(user)
        verify(userRepository, times(1)).addUser(user)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when passwords do not match`() = runTest {
        // Arrange
        val user = User(email = "test@example.com", password = "password123")
        val confirmationPassword = "differentPassword"

        // Act
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                registrationUseCase(user, confirmationPassword)
            }
        }
        // Assert (Exception is expected)
        val expectedExceptionMsg="Passwords do not match"
        assertEquals(expectedExceptionMsg, actualException.message)
        verify(authRepository, never()).register(user)
        verify(userRepository, never()).addUser(any())
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
        verify(userRepository, never()).addUser(any())
    }
}