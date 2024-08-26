package com.compose.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.User
import com.compose.domain.usecases.GetCachedUserUseCase
import com.compose.presentation.R
import com.compose.presentation.events.StartDestinationEvent
import com.compose.presentation.events.StartDestinationEvent.*
import com.compose.presentation.intents.NotificationPermissionCommand
import com.compose.presentation.intents.NotificationPermissionCommand.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCachedUserUseCase: GetCachedUserUseCase
) : ViewModel() {

    private val _navigationEvent = MutableSharedFlow<StartDestinationEvent>()
    val navigationCommand = _navigationEvent.asSharedFlow()

    private val _notificationPermissionCommand =
        MutableStateFlow<NotificationPermissionCommand?>(null)
    val notificationPermissionCommand: StateFlow<NotificationPermissionCommand?> get() = _notificationPermissionCommand.asStateFlow()
    fun checkNotificationPermission() {
        _notificationPermissionCommand.value = Request
    }
    fun onNotificationPermissionResult(isGranted: Boolean) {
        if (!isGranted) {
            _notificationPermissionCommand.value =
                ShowDenied
        }
    }
    fun onShowPermissionRationale() {
        _notificationPermissionCommand.value = ShowRationale
    }

    init {
        observeLoginStatus()
    }
    private fun observeLoginStatus() {
        var cachedUser: User? = null
        var destination: Int
        viewModelScope.launch {
            try {
                cachedUser = withContext(Dispatchers.IO) { getCachedUserUseCase() }
                destination = R.id.homeFragment
            } catch (e: Exception) {
                destination = R.id.loginFragment
            }
            _navigationEvent.emit(To(destination, cachedUser))
        }
    }
}