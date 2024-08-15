package com.compose.data.datasource.user

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.compose.data.constatnts.PreferenceDataStoreConstants
import com.compose.domain.entities.User
import kotlinx.coroutines.flow.first

class UserPreferencesDataSource(private val dataStore: DataStore<Preferences>):IUserPreferencesDataSource {

    // Cache user details into DataStore
    override suspend fun cacheUser(user: User) {
        dataStore.edit { preferences ->
            preferences[PreferenceDataStoreConstants.USER_ID] = user.userId
            preferences[PreferenceDataStoreConstants.USERNAME] = user.username
            preferences[PreferenceDataStoreConstants.USER_EMAIL] = user.email
            preferences[PreferenceDataStoreConstants.USER_PROFILE_PIC] = user.profilePictureUrl
            preferences[PreferenceDataStoreConstants.USER_PASSWORD] = user.password
        }
    }

    // Clear user details from DataStore
    override suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(PreferenceDataStoreConstants.USER_ID)
            preferences.remove(PreferenceDataStoreConstants.USERNAME)
            preferences.remove(PreferenceDataStoreConstants.USER_EMAIL)
            preferences.remove(PreferenceDataStoreConstants.USER_PASSWORD)
            preferences.remove(PreferenceDataStoreConstants.USER_PROFILE_PIC)
        }
    }

    // Retrieve user details from DataStore
    override suspend fun getUser(): User? {
        val preferences = dataStore.data.first()
        val userId = preferences[PreferenceDataStoreConstants.USER_ID] ?: return null
        val username = preferences[PreferenceDataStoreConstants.USERNAME] ?: return null
        val email = preferences[PreferenceDataStoreConstants.USER_EMAIL] ?: return null
        val password = preferences[PreferenceDataStoreConstants.USER_PASSWORD] ?: return null
        val profilePic = preferences[PreferenceDataStoreConstants.USER_PROFILE_PIC] ?: ""

        return User(
            userId = userId,
            username = username,
            email = email,
            password = password,
            profilePictureUrl = profilePic
        )
    }
}