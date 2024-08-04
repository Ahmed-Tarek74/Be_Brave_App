package com.compose.chatapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.compose.presentation.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NotificationService : FirebaseMessagingService() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase


    override fun onCreate() {
        super.onCreate()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            val tokensRef = database.reference.child("users_tokens").child(it)
            tokensRef.setValue(token)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationData = message.data

        val title = notificationData["title"]
        val body = notificationData["body"]
        val senderUsername = notificationData["senderUsername"]
        val senderImage = notificationData["senderImage"]

        // Check if the necessary fields are not null
        if (title != null && body != null) {
            // Create and show the notification
            showNotification(title, body, senderUsername, senderImage)
        } else {
            Log.e("NotificationService", "Missing notification data: $notificationData")
        }
    }
    @SuppressLint("MissingPermission")
    private fun showNotification(
        title: String,
        body: String,
        senderUsername: String?,
        senderImage: String?
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("$senderUsername: $title")
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        senderImage?.let {
            val bitmap = getBitmapFromURL(it)
            if (bitmap != null) {
                notificationBuilder.setLargeIcon(bitmap)
            } else {
                notificationBuilder.setSmallIcon(R.drawable.default_profile_picture)
            }
        }

        with(NotificationManagerCompat.from(this)) {
            // Create notification channel if necessary
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Default Channel for App Notifications"
                }
                createNotificationChannel(channel)
            }
            // Show the notification
            notify(0, notificationBuilder.build())
        }
    }

    private fun getBitmapFromURL(url: String): Bitmap? {
        return try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}
