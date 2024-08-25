package com.compose.presentation.events
import com.compose.domain.entities.User
sealed class StartDestinationEvent {
    data class To(val destination: Int, val user: User? = null) : StartDestinationEvent()
}