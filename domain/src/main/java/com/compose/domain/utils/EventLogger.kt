package com.compose.domain.utils

interface EventLogger {
    fun logEvent(eventName: String, params: Map<String, String> = emptyMap())
}