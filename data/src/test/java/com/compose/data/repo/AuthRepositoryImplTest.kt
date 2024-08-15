package com.compose.data.repo

import com.compose.data.datasource.auth.IAuthentication
import com.compose.data.datasource.user.IFirebaseUserDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any

class AuthRepositoryImplTest {

    private lateinit var firebaseAuth: IAuthentication
    private lateinit var userDataSource: IFirebaseUserDataSource
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        firebaseAuth = mock(IAuthentication::class.java)
        userDataSource = mock(IFirebaseUserDataSource::class.java)
        authRepository = AuthRepositoryImpl(firebaseAuth, userDataSource)
    }

    @Test
    fun `login should return user successfully`() = runTest {
        val email = "email@example.com"
        val password = "password123"
        val userId = "user123"
        val username = "username"
        val expectedUser =
            User(userId = userId, username = username, email = email, password = password)
        `when`(firebaseAuth.login(email, password)).thenReturn(userId)
        `when`(userDataSource.getUserById(userId)).thenReturn(expectedUser)
        val result = authRepository.login(email, password)
        assertEquals(expectedUser, result)
        verify(firebaseAuth).login(email, password)
        verify(userDataSource).getUserById(userId)
    }

    @Test
    fun `login should throw exception when user is not found`() = runTest {
        val email = "email@example.com"
        val password = "password123"
        val userId = "user123"

        `when`(firebaseAuth.login(email, password)).thenReturn(userId)
        `when`(userDataSource.getUserById(userId)).thenReturn(null)

        val exception = assertThrows(Exception::class.java) {
            runBlocking {
                authRepository.login(email, password)
            }
        }
        val expectedExceptionMsg = "Login failed: User not found."
        assertEquals(expectedExceptionMsg, exception.message)
        verify(firebaseAuth).login(email, password)
        verify(userDataSource).getUserById(userId)
    }

    @Test
    fun `login should throw exception when login fails`() = runTest {
        val email = "email@example.com"
        val password = "password123"
        val exceptionMessage = "Login service unavailable"
        `when`(firebaseAuth.login(email, password)).thenThrow(RuntimeException(exceptionMessage))
        val exception = assertThrows(Exception::class.java) {
            runBlocking {
                authRepository.login(email, password)
            }
        }
        val expectedExceptionMsg = "Login failed: $exceptionMessage"
        assertEquals(expectedExceptionMsg, exception.message)
        verify(firebaseAuth).login(email, password)
        verify(userDataSource, never()).getUserById(anyString())
    }

    @Test
    fun `register should return registered user successfully`() = runTest {
        val email = "email@example.com"
        val password = "password123"
        val userId = "user123"
        val user = User("", "username", email, password)
        val expectedUser = user.copy(userId = userId)

        `when`(firebaseAuth.register(email, password)).thenReturn(userId)
        `when`(userDataSource.saveUser(expectedUser)).thenReturn(Unit)

        val result = authRepository.register(user)

        assertEquals(expectedUser, result)
        verify(firebaseAuth).register(email, password)
        verify(userDataSource).saveUser(expectedUser)
    }

    @Test
    fun `register should throw exception when registration fails`() = runTest {
        val email = "email@example.com"
        val password = "password123"
        val user = User("", "username", email, password)
        val exceptionMessage = "Registration service unavailable"

        `when`(firebaseAuth.register(email, password)).thenThrow(RuntimeException(exceptionMessage))
        val exception = assertThrows(Exception::class.java) {
            runBlocking { authRepository.register(user) }

        }
        val expectedExceptionMsg = "Registration failed: $exceptionMessage"
        assertEquals(expectedExceptionMsg, exception.message)
        verify(firebaseAuth).register(email, password)
        verify(userDataSource, never()).saveUser(any())
    }

    @Test
    fun `logOut should complete successfully`() = runTest {
        authRepository.logOut()
        verify(firebaseAuth).logout()
    }

    @Test
    fun `logOut should throw exception when logout fails`() = runTest {
        val exceptionMessage = "Logout service unavailable"

        `when`(firebaseAuth.logout()).thenThrow(RuntimeException(exceptionMessage))

        val exception = assertThrows(Exception::class.java) {
            runBlocking { authRepository.logOut() }
        }
        val expectedExceptionMsg="Logout failed: $exceptionMessage"

        assertEquals(expectedExceptionMsg, exception.message)
        verify(firebaseAuth).logout()
    }
}