package com.compose.data.repo

import com.compose.data.datasource.deviceToken.DeviceTokenException
import com.compose.data.datasource.deviceToken.DeviceTokenDataSource
import com.compose.domain.repos.DeviceTokenRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class DeviceTokenRepositoryImplTest {

    private lateinit var deviceTokenDataSource: DeviceTokenDataSource
    private lateinit var deviceTokenRepository: DeviceTokenRepository

    @Before
    fun setUp() {
        deviceTokenDataSource = mock()
        deviceTokenRepository = DeviceTokenRepositoryImpl(deviceTokenDataSource)
    }
    @Test
    fun `getDeviceToken should return token when successful`() =runTest {
        val userId="123"
        val expectedToken="token456"
        whenever(deviceTokenDataSource.getDeviceToken(userId)).thenReturn(expectedToken)
        val actualToken = deviceTokenRepository.getDeviceToken(userId)
        assertEquals(expectedToken, actualToken)
    }
    @Test
    fun `getDeviceToken should throw DeviceTokenException when retrieval fails`() = runTest {
        val userId = "user123"
        whenever(deviceTokenDataSource.getDeviceToken(userId)).thenThrow(RuntimeException("Token retrieval failed"))

        val actualException = assertThrows(DeviceTokenException::class.java) {
          runBlocking {
                deviceTokenRepository.getDeviceToken(userId)
            }
        }
        val expectedExceptionMsg="Failed to retrieve device token for user: $userId"
        assertEquals(expectedExceptionMsg, actualException.message)
    }
    @Test
    fun `saveDeviceToken should save token when successful`() = runTest {
        val userId = "user123"
        whenever(deviceTokenDataSource.setDeviceTokenToUser(userId)).thenReturn(Unit)
        //act
        deviceTokenRepository.setDeviceTokenToUser(userId)
        //assert
        verify(deviceTokenDataSource).setDeviceTokenToUser(userId)
    }
    @Test
    fun `saveDeviceToken should throw DeviceTokenException when it fails to save token`() = runTest {
        val userId = "user123"
        whenever(deviceTokenDataSource.setDeviceTokenToUser(userId)).thenThrow(RuntimeException("Failed to save token"))
        val actualException = assertThrows(DeviceTokenException::class.java) {
            runBlocking {
                deviceTokenRepository.setDeviceTokenToUser(userId)
            }
        }
        val expectedExceptionMsg="Failed to save device token for user: $userId"
        assertEquals(expectedExceptionMsg,actualException.message)
    }

}