package com.compose.data.utils

import com.compose.domain.utils.EventLogger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

class FirebaseEventLogger @Inject constructor(
    private val crashlytics: FirebaseCrashlytics
) : EventLogger {
    override fun logEvent(eventName: String, params: Map<String, String>) {
        crashlytics.log(eventName)
        params.forEach { (key, value) ->
            crashlytics.setCustomKey(key, value)
        }
    }
}