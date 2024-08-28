package com.compose.data.datasource.user

interface UserDataSource : RemoteUserDataManager, UserPreferencesManager {
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())

}