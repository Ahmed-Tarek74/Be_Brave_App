package com.compose.presentation.events

import android.os.Bundle

sealed class StartDestinationEvent {
    data class To(val destination: Int, val args: Bundle? = null) : StartDestinationEvent()
}