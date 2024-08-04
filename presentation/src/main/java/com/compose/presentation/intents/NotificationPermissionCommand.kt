package com.compose.presentation.intents

sealed class NotificationPermissionCommand {
    data object Request : NotificationPermissionCommand()
    data object ShowDenied : NotificationPermissionCommand()
    data object ShowRationale : NotificationPermissionCommand()
}
