package com.compose.data.services


interface ITokenService {
    suspend fun getToken(): String
}