package com.compose.data.repo

import com.compose.data.datasource.user.IFirebaseUserDataSource
import com.compose.domain.entities.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class GetUsersRepositoryImplTest {

    private lateinit var IFirebaseUserDataSource: IFirebaseUserDataSource
    private lateinit var getUsersRepository: GetUsersRepositoryImpl

    @Before
    fun setUp() {
        IFirebaseUserDataSource = mock()
        getUsersRepository = GetUsersRepositoryImpl(IFirebaseUserDataSource)
    }

    @Test
    fun `searchUsers should return filtered users when successful`() = runTest {
        val searchQuery = "User"
        val homeUserId = "userId#1"
        val user1 = User("userId#1", "User One")
        val user2 = User("userId#2", "User Two")
        val user3 = User("userId#3", "User Three")
        whenever(IFirebaseUserDataSource.getUsersByUsername(searchQuery)).thenReturn(
            listOf(user1, user2, user3)
        )

        val actual = getUsersRepository.searchUsers(searchQuery, homeUserId)
        val expected=listOf(user2, user3)
        assertEquals(expected, actual)
    }

    @Test
    fun `searchUsers should throw exception when data source fails`() = runBlocking {
        val searchQuery = "user"
        val homeUserId = "userId#1"

        whenever(IFirebaseUserDataSource.getUsersByUsername(searchQuery)).thenThrow(RuntimeException("Data source Failure"))

        val actualException = assertThrows(Exception::class.java) {
            runBlocking {
                getUsersRepository.searchUsers(searchQuery, homeUserId)
            }
        }
        assertEquals("Failed to search users with query: $searchQuery", actualException.message)
    }

    @Test
    fun `getUserById should return user when successful`() = runTest {
        val userId = "user123"
        val user = User(userId, "User Name")

        whenever(IFirebaseUserDataSource.getUserById(userId)).thenReturn(user)

        val result = getUsersRepository.getUserById(userId)

        assertEquals(user, result)
    }

    @Test
    fun `getUserById should throw exception when user not found`() = runTest {
        val userId = "user123"
        whenever(IFirebaseUserDataSource.getUserById(userId)).thenReturn(null)
        val actualException = assertThrows(Exception::class.java) {
            runBlocking { getUsersRepository.getUserById(userId) }
        }
        val expectedExceptionMsg="Failed to retrieve user with ID: $userId"
        assertEquals(expectedExceptionMsg,actualException.message)
    }
    @Test
    fun `getUserById should throw exception when data source fails`() = runTest {
        val userId = "user123"

        whenever(IFirebaseUserDataSource.getUserById(userId)).thenThrow(RuntimeException("Data source fails"))

        val actualException = assertThrows(Exception::class.java) {
            runBlocking { getUsersRepository.getUserById(userId) }
        }
        val expectedExceptionMsg="Failed to retrieve user with ID: $userId"
        assertEquals(expectedExceptionMsg, actualException.message)
    }
}