package com.compose.domain.usecases
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
class LogOutUseCaseTest {
    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var userRepository: UserRepository

    private lateinit var logOutUseCase: LogOutUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        logOutUseCase = LogOutUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke should call logOut and clearUserPreferences`() = runTest {
        // Act
        logOutUseCase()

        // Assert
        verify(authRepository, times(1)).logOut()
        verify(userRepository, times(1)).clearUserPreferences()
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when logOut fails`() = runTest {
        // Arrange
        `when`(authRepository.logOut()).thenThrow(Exception("Logout failed"))

        // Act
        logOutUseCase()

        // Assert (Exception is expected)
        verify(authRepository, times(1)).logOut()
        verify(userRepository, times(1)).clearUserPreferences()
    }

    @Test(expected = Exception::class)
    fun `invoke should throw exception when clearUserPreferences fails`() = runTest {
        // Arrange
        `when`(userRepository.clearUserPreferences()).thenThrow(Exception("Failed to clear user preferences"))

        // Act
        logOutUseCase()

        // Assert (Exception is expected)
        verify(authRepository, times(1)).logOut()
        verify(userRepository, never()).clearUserPreferences()
    }
}