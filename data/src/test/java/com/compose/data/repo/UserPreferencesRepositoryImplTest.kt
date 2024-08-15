package com.compose.data.repo

import com.compose.data.datasource.user.IUserPreferencesDataSource
import com.compose.domain.entities.User
import com.compose.domain.repos.UserPreferencesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

class UserPreferencesRepositoryImplTest {

    private lateinit var userPreferencesDataSource: IUserPreferencesDataSource
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userPreferencesDataSource = mock(IUserPreferencesDataSource::class.java)
        userPreferencesRepository = UserPreferencesRepositoryImpl(userPreferencesDataSource)
    }

    @Test
    fun `saveUserPreferences should save user successfully`() = runTest {
        val user = User("userId", "username", "email@example.com")
        userPreferencesRepository.saveUserPreferences(user)
        verify(userPreferencesDataSource).cacheUser(user)
    }

    @Test
    fun `saveUserPreferences should throw exception when caching fails`() = runTest {
        val user = User("userId", "username", "email@example.com")
        val exceptionMessage = "Failed to cache user"
        whenever(userPreferencesDataSource.cacheUser(user)).thenThrow(
            RuntimeException(exceptionMessage)
        )
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userPreferencesRepository.saveUserPreferences(user)
            }
        }
        val existsExceptionMsg="Failed to cache user: $exceptionMessage"
        assertEquals(existsExceptionMsg, actualException.message)
    }

    @Test
    fun `getCachedUser should return cached user successfully`() = runTest {
        val user = User("userId", "username", "email@example.com")
        `when`(userPreferencesDataSource.getUser()).thenReturn(user)
        val result = userPreferencesRepository.getCachedUser()
        assertEquals(user, result)
        verify(userPreferencesDataSource).getUser()
    }

    @Test
    fun `getCachedUser should throw exception when no user is cached`() = runTest {
        `when`(userPreferencesDataSource.getUser()).thenReturn(null)
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userPreferencesRepository.getCachedUser()
            }
        }
        assertEquals("Failed to get logged in user", actualException.message)
    }
    @Test
    fun `getCachedUser should throw exception when retrieval fails`() = runTest {
        val exceptionMessage = "Failed to retrieve cached user"
        `when`(userPreferencesDataSource.getUser()).thenThrow(RuntimeException(exceptionMessage))
        val actualException =assertThrows(Exception::class.java) {
            runBlocking {
                userPreferencesRepository.getCachedUser()
            }
        }
        val expectedExceptionMsg="Failed to get logged in user: $exceptionMessage"
        assertEquals(expectedExceptionMsg, actualException.message)
    }
    @Test
    fun `clearUserPreferences should clear user preferences successfully`() = runTest {
        userPreferencesRepository.clearUserPreferences()
        verify(userPreferencesDataSource).clearUser()
    }

    @Test
    fun `clearUserPreferences should throw exception when clearing fails`() = runTest {
        val exceptionMessage = "Clear operation failed"
        doThrow(RuntimeException(exceptionMessage)).`when`(userPreferencesDataSource).clearUser()

        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userPreferencesRepository.clearUserPreferences()
            }
        }
        val expectedExceptionMsg="Failed to clear user preferences: $exceptionMessage"

        assertEquals(expectedExceptionMsg, actualException.message)
    }
}