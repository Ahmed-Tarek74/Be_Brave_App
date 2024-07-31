package com.compose.data.local
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.compose.domain.entities.User
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(PreferenceDataStoreConstants.DATASTORE_NAME)

class DataStoreManager(private val context: Context) {

    suspend fun cacheUser(user: User) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceDataStoreConstants.USER_ID] = user.userId
            preferences[PreferenceDataStoreConstants.USERNAME] = user.username
            preferences[PreferenceDataStoreConstants.USER_EMAIL] = user.email
            preferences[PreferenceDataStoreConstants.USER_PROFILE_PIC] = user.profilePictureUrl
            preferences[PreferenceDataStoreConstants.USER_PASSWORD] = user.password
        }
    }

    suspend fun clearUser() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferenceDataStoreConstants.USER_ID)
            preferences.remove(PreferenceDataStoreConstants.USERNAME)
            preferences.remove(PreferenceDataStoreConstants.USER_EMAIL)
            preferences.remove(PreferenceDataStoreConstants.USER_PASSWORD)
            preferences.remove(PreferenceDataStoreConstants.USER_PROFILE_PIC)
        }
    }
    suspend fun getUser(): User? {
        val preferences = context.dataStore.data.first()
        val userId = preferences[PreferenceDataStoreConstants.USER_ID] ?: ""
        val username = preferences[PreferenceDataStoreConstants.USERNAME] ?: ""
        val email = preferences[PreferenceDataStoreConstants.USER_EMAIL] ?: ""
        val password = preferences[PreferenceDataStoreConstants.USER_PASSWORD] ?: ""
        val profilePic = preferences[PreferenceDataStoreConstants.USER_PROFILE_PIC] ?: ""
        return if (userId.isNotEmpty() && username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            User(
                userId = userId,
                username = username,
                email = email,
                password = password,
                profilePictureUrl = profilePic
            )
        } else {
            null
        }
    }
}