package com.compose.domain.usecases

import com.compose.domain.base.BaseUseCaseTest
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class LoginUseCaseTest : BaseUseCaseTest() {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var deviceTokenRepository: DeviceTokenRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var loginUseCase: LoginUseCase


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(authRepository, deviceTokenRepository, userRepository)
    }

    @Test
    fun `invoke should login, get user by ID, save user preferences, and set device token to user`() =
        runTest(testDispatcher) {
            // Arrange
            val email = "test@example.com"
            val password = "password123"
            val userId = "userId#123"
            val user = User(userId = userId, email = email, password = password)

            `when`(authRepository.login(email, password)).thenReturn(userId)
            `when`(userRepository.getUserById(userId)).thenReturn(user)

            // Act
            val result = loginUseCase(email, password)

            // Assert
            assert(result == user)
            verify(authRepository, times(1)).login(email, password)
            verify(userRepository, times(1)).getUserById(userId)
            verify(userRepository, times(1)).saveUserPreferences(user)
            verify(deviceTokenRepository, times(1)).setDeviceTokenToUser(userId)
        }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when login fails`() = runTest(testDispatcher) {
        // Arrange
        val email = "test@example.com"
        val password = "password123"

        `when`(authRepository.login(email, password)).thenThrow(Exception("Login failed"))

        // Act
        loginUseCase(email, password)

        // Assert
        verify(authRepository, times(1)).login(email, password)
        verify(userRepository, never()).getUserById(anyString())
        verify(userRepository, never()).saveUserPreferences(any())
        verify(deviceTokenRepository, never()).setDeviceTokenToUser(anyString())
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when getting user by ID fails`() = runTest(testDispatcher) {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userId = "userId#123"

        `when`(authRepository.login(email, password)).thenReturn(userId)
        `when`(userRepository.getUserById(userId)).thenThrow(Exception("Failed to retrieve user"))

        // Act
        loginUseCase(email, password)

        // Assert
        verify(authRepository, times(1)).login(email, password)
        verify(userRepository, times(1)).getUserById(userId)
        verify(userRepository, never()).saveUserPreferences(any())
        verify(deviceTokenRepository, never()).setDeviceTokenToUser(anyString())
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when saving user preferences fails`() =
        runTest(testDispatcher) {
            // Arrange
            val email = "test@example.com"
            val password = "password123"
            val userId = "userId#123"
            val user = User(userId = userId, email = email, password = password)

            `when`(authRepository.login(email, password)).thenReturn(userId)
            `when`(userRepository.getUserById(userId)).thenReturn(user)
            `when`(userRepository.saveUserPreferences(user)).thenThrow(Exception("Failed to save user preferences"))

            // Act
            loginUseCase(email, password)

            // Assert
            verify(authRepository, times(1)).login(email, password)
            verify(userRepository, times(1)).getUserById(userId)
            verify(userRepository, times(1)).saveUserPreferences(user)
            verify(deviceTokenRepository, never()).setDeviceTokenToUser(anyString())
        }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when setting device token to user fails`() =
        runTest(testDispatcher) {
            // Arrange
            val email = "test@example.com"
            val password = "password123"
            val userId = "userId#123"
            val user = User(userId = userId, email = email, password = password)

            `when`(authRepository.login(email, password)).thenReturn(userId)
            `when`(userRepository.getUserById(userId)).thenReturn(user)
            `when`(deviceTokenRepository.setDeviceTokenToUser(userId)).thenThrow(Exception("Failed to set device token to user"))

            // Act
            loginUseCase(email, password)

            // Assert
            verify(authRepository, times(1)).login(email, password)
            verify(userRepository, times(1)).getUserById(userId)
            verify(userRepository, times(1)).saveUserPreferences(user)
            verify(deviceTokenRepository, times(1)).setDeviceTokenToUser(userId)
        }
}
