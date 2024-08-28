package com.compose.data.datasource.auth

interface AuthenticationDataSource {
    suspend fun login(email: String, password: String): String
    suspend fun register(email: String, password: String): String
    fun logout()
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())

}