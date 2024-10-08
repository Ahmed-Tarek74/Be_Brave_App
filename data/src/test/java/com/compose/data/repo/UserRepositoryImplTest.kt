package com.compose.data.repo

import com.compose.data.datasource.user.UserDataSourceImpl
import com.compose.domain.entities.User
import com.compose.domain.repos.UserRepository
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
import org.mockito.kotlin.times
import org.mockito.kotlin.whenever

class UserRepositoryImplTest {

    private lateinit var userDataSourceImpl: UserDataSourceImpl
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userDataSourceImpl = mock()
        userRepository = UserRepositoryImpl(userDataSourceImpl)
    }

    @Test
    fun `searchUsers should return filtered users when successful`() = runTest {
        val searchQuery = "User"
        val homeUserId = "userId#1"
        val user1 = User("userId#1", "User One")
        val user2 = User("userId#2", "User Two")
        val user3 = User("userId#3", "User Three")
        whenever(userDataSourceImpl.getUsersByUsername(searchQuery)).thenReturn(
            listOf(user1, user2, user3)
        )
        val actual = userRepository.searchUsers(searchQuery, homeUserId)
        val expected = listOf(user2, user3)
        assertEquals(expected, actual)
    }

    @Test
    fun `searchUsers should throw exception when data source fails`() = runBlocking {
        val searchQuery = "user"
        val homeUserId = "userId#1"

        whenever(userDataSourceImpl.getUsersByUsername(searchQuery)).thenThrow(RuntimeException("Data source Failure"))

        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userRepository.searchUsers(searchQuery, homeUserId)
            }
        }
        assertEquals("Failed to search users with query: $searchQuery", actualException.message)
    }

    @Test
    fun `getUserById should return user when successful`() = runTest {
        val userId = "user123"
        val user = User(userId, "User Name")

        whenever(userDataSourceImpl.getUserById(userId)).thenReturn(user)

        val result = userRepository.getUserById(userId)

        assertEquals(user, result)
    }

    @Test
    fun `getUserById should throw exception when user not found`() = runTest {
        val userId = "user123"
        whenever(userDataSourceImpl.getUserById(userId)).thenReturn(null)
        val actualException = assertThrows(Exception::class.java) {
            runBlocking { userRepository.getUserById(userId) }
        }
        val expectedExceptionMsg = "Failed to retrieve user with ID: $userId"
        assertEquals(expectedExceptionMsg, actualException.message)
    }

    @Test
    fun `getUserById should throw exception when data source fails`() = runTest {
        val userId = "user123"

        whenever(userDataSourceImpl.getUserById(userId)).thenThrow(RuntimeException("Data source fails"))

        val actualException = assertThrows(Exception::class.java) {
            runBlocking { userRepository.getUserById(userId) }
        }
        val expectedExceptionMsg = "Failed to retrieve user with ID: $userId"
        assertEquals(expectedExceptionMsg, actualException.message)
    }

    @Test
    fun `saveUserPreferences should save user successfully`() = runTest {
        val user = User("userId", "username", "email@example.com")
        whenever(userDataSourceImpl.cacheUser(user)).thenReturn(Unit)
        //Act
        userRepository.saveUserPreferences(user)

        verify(userDataSourceImpl, times(1)).cacheUser(user)
    }

    @Test
    fun `saveUserPreferences should throw exception when caching fails`() = runTest {
        val user = User("userId", "username", "email@example.com")
        val exceptionMessage = "Failed to cache user"
        whenever(userDataSourceImpl.cacheUser(user)).thenThrow(
            RuntimeException(exceptionMessage)
        )
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userRepository.saveUserPreferences(user)
            }
        }
        val existsExceptionMsg = "Failed to cache user: $exceptionMessage"
        assertEquals(existsExceptionMsg, actualException.message)
    }

    @Test
    fun `getCachedUser should return cached user successfully`() = runTest {
        val user = User("userId", "username", "email@example.com")
        `when`(userDataSourceImpl.getCachedUser()).thenReturn(user)
        val result = userRepository.getCachedUser()
        assertEquals(user, result)
        verify(userDataSourceImpl, times(1)).getCachedUser()
    }

    @Test
    fun `getCachedUser should return null when no user is cached`() = runTest {
        `when`(userDataSourceImpl.getCachedUser()).thenReturn(null)
        val actual = userRepository.getCachedUser()
        assertEquals(null, actual)
    }

    @Test
    fun `getCachedUser should throw exception when retrieval fails`() = runTest {
        val exceptionMessage = "Failed to retrieve cached user"
        `when`(userDataSourceImpl.getCachedUser()).thenThrow(RuntimeException(exceptionMessage))
        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userRepository.getCachedUser()
            }
        }
        assertEquals(exceptionMessage, actualException.message)
    }

    @Test
    fun `clearUserPreferences should clear user preferences successfully`() = runTest {
        `when`(userDataSourceImpl.clearUser()).thenReturn(Unit)
        //Act
        userRepository.clearUserPreferences()
        //Assert
        verify(userDataSourceImpl, times(1)).clearUser()
    }

    @Test
    fun `clearUserPreferences should throw exception when clearing fails`() = runTest {
        val exceptionMessage = "Clear operation failed"
        doThrow(RuntimeException(exceptionMessage)).`when`(userDataSourceImpl).clearUser()

        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                userRepository.clearUserPreferences()
            }
        }
        val expectedExceptionMsg = "Failed to clear user preferences: $exceptionMessage"

        assertEquals(expectedExceptionMsg, actualException.message)
    }
}