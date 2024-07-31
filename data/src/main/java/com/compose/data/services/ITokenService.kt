package com.compose.data.services

import kotlinx.coroutines.flow.Flow

interface ITokenService {
    fun getToken(): Flow<String>
}