package com.compose.data.utils

import android.content.res.AssetManager
import com.google.auth.oauth2.GoogleCredentials
import java.io.InputStream

class FcmServiceUtil(private val assetManager: AssetManager) {

    private  val SCOPES = listOf("https://www.googleapis.com/auth/cloud-platform")

    fun getAccessToken(): String {
        return try {
            // Read the service account JSON from assets
            val inputStream: InputStream = assetManager.open("service-account-file.json")

            // Create GoogleCredentials from the input stream
            val googleCredentials = GoogleCredentials.fromStream(inputStream)
                .createScoped(SCOPES)

            // Refresh the credentials and get the access token
            googleCredentials.refreshIfExpired()
            googleCredentials.accessToken?.tokenValue
                ?: throw AccessTokenException("Access token is null")
        } catch (e: Exception) {
            throw AccessTokenException("Failed to get access token: ${e.message}", e)
        }
    }
    class AccessTokenException(message: String, cause: Throwable? = null) : Exception(message, cause)
}
