package com.compose.domain.usecases
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserPreferencesRepository
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

class LoginUseCaseTest {
    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var deviceTokenRepository: DeviceTokenRepository

    @Mock
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        loginUseCase = LoginUseCase(authRepository, deviceTokenRepository, userPreferencesRepository)
    }

    @Test
    fun `invoke should login, save user preferences, get and save device token`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userId="userId#123"
        val user = User(userId =userId , email = email, password = password)
        val token = "deviceToken123"

        `when`(authRepository.login(email, password)).thenReturn(user)
        `when`(deviceTokenRepository.getDeviceToken()).thenReturn(token)

        // Act
        val result = loginUseCase(email, password)

        // Assert
        assert(result == user)
        verify(authRepository, times(1)).login(email, password)
        verify(userPreferencesRepository, times(1)).saveUserPreferences(user)
        verify(deviceTokenRepository, times(1)).getDeviceToken()
        verify(deviceTokenRepository, times(1)).saveDeviceToken(userId, token)
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when login fails`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"

        `when`(authRepository.login(email, password)).thenThrow(Exception("Login failed"))

        // Act
        loginUseCase(email, password)

        // Assert
        verify(authRepository, times(1)).login(email, password)
        verify(userPreferencesRepository, never()).saveUserPreferences(any())
        verify(deviceTokenRepository, never()).getDeviceToken()
        verify(deviceTokenRepository, never()).saveDeviceToken(anyString(), anyString())
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when getting device token fails`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userId="userId#123"
        val user = User(userId =userId , email = email, password = password)

        `when`(authRepository.login(email, password)).thenReturn(user)
        `when`(deviceTokenRepository.getDeviceToken()).thenThrow(Exception("Failed to retrieve device token"))

        // Act
        loginUseCase(email, password)

        // Assert
        verify(authRepository, times(1)).login(email, password)
        verify(userPreferencesRepository, times(1)).saveUserPreferences(user)
        verify(deviceTokenRepository, times(1)).getDeviceToken()
        verify(deviceTokenRepository, never()).saveDeviceToken(anyString(), anyString())
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when saving device token fails`() = runTest {
        // Arrange
        val email = "test@example.com"
        val password = "password123"
        val userId="userId#123"
        val user = User(userId, email = email, password = password)
        val token = "deviceToken123"

        `when`(authRepository.login(email, password)).thenReturn(user)
        `when`(deviceTokenRepository.getDeviceToken()).thenReturn(token)
        `when`(deviceTokenRepository.saveDeviceToken(user.userId, token)).thenThrow(Exception("Failed to save device token"))

        // Act
        loginUseCase(email, password)

        // Assert
        verify(authRepository, times(1)).login(email, password)
        verify(userPreferencesRepository, times(1)).saveUserPreferences(user)
        verify(deviceTokenRepository, times(1)).getDeviceToken()
        verify(deviceTokenRepository, times(1)).saveDeviceToken(userId, token)
    }
}