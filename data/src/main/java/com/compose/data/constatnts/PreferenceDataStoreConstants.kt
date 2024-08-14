package com.compose.data.constatnts

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceDataStoreConstants {
    const val DATASTORE_NAME = "user_prefs"
    val USER_ID = stringPreferencesKey("user_id")
    val USERNAME = stringPreferencesKey("username")
    val USER_EMAIL = stringPreferencesKey("user_email")
    val USER_PASSWORD = stringPreferencesKey("user_password")
    val USER_PROFILE_PIC = stringPreferencesKey("user_profile_pic")
}