package com.compose.data.datasource.auth

interface IAuthentication {
    suspend fun login(email: String, password: String): String
    suspend fun register(email: String, password: String): String
    fun logout()
}