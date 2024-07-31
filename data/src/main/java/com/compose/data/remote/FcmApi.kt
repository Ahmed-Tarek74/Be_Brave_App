package com.compose.data.remote
import com.compose.domain.entities.NotificationMessage
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmApi {
    companion object{
        private const val PROJECT_ID = "chat-app-a59c4"
        const val SEND_NOTIFICATION_ENDPOINT = "projects/$PROJECT_ID/messages:send"
    }
    @POST(SEND_NOTIFICATION_ENDPOINT)
     suspend fun sendNotification(
        @Body notification: NotificationMessage
    ): Response<Unit>
}