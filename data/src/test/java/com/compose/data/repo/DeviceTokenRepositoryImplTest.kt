package com.compose.data.repo

import com.compose.data.datasource.deviceToken.DeviceTokenException
import com.compose.data.datasource.deviceToken.IDeviceTokenDataSource
import com.compose.data.services.ITokenService
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class DeviceTokenRepositoryImplTest {

    private lateinit var tokenService: ITokenService
    private lateinit var deviceTokenDataSource: IDeviceTokenDataSource
    private lateinit var deviceTokenRepository: DeviceTokenRepositoryImpl

    @Before
    fun setUp() {
        tokenService = mock()
        deviceTokenDataSource = mock()
        deviceTokenRepository = DeviceTokenRepositoryImpl(tokenService, deviceTokenDataSource)
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
        val token = "token456"
        whenever(deviceTokenDataSource.saveDeviceToken(userId, token)).thenReturn(Unit)
        deviceTokenRepository.saveDeviceToken(userId,token)
        verify(deviceTokenDataSource).saveDeviceToken(userId, token)
    }
    @Test
    fun `saveDeviceToken should throw DeviceTokenException when it fails to save token`() = runTest {
        val userId = "user123"
        val token = "token456"
        whenever(deviceTokenDataSource.saveDeviceToken(userId, token)).thenThrow(RuntimeException("Failed to save token"))

        val actualException = assertThrows(DeviceTokenException::class.java) {
            runBlocking {
                deviceTokenRepository.saveDeviceToken(userId,token)
            }
        }
        val expectedExceptionMsg="Failed to save device token for user: $userId"
        assertEquals(expectedExceptionMsg,actualException.message)
    }

}