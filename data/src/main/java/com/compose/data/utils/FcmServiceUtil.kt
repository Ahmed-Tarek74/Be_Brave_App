package com.compose.data.utils

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import java.io.InputStream
import java.util.logging.Logger



class FcmServiceUtil(private val context: Context) {

    private val SCOPES = listOf("https://www.googleapis.com/auth/cloud-platform")
    private val logger: Logger = Logger.getLogger(FcmServiceUtil::class.java.name)
    fun getAccessToken(): String? {
        return try {
            // Read the service account JSON from assets
            val inputStream: InputStream = context.assets.open("service-account-file.json")
            // Create GoogleCredentials from the input stream
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(SCOPES)
            // Refresh the credentials and get the access token
            googleCredentials.refreshIfExpired()
            googleCredentials.accessToken?.tokenValue
        } catch (e: Exception) {
            logger.severe("Failed to get access token: ${e.message}")
            null
        }
    }
}