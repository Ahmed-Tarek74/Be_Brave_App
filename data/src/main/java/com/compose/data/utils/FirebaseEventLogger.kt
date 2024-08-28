package com.compose.data.utils

import android.os.Bundle
import com.compose.domain.utils.EventLogger
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseEventLogger @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
) : EventLogger {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                putString(key, value)
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }
}