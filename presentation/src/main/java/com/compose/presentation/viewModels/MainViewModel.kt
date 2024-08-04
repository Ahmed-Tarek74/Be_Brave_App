package com.compose.presentation.viewModels

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.usecases.GetCachedUserUseCase
import com.compose.presentation.R
import com.compose.presentation.events.NavigationEvent
import com.compose.presentation.intents.NotificationPermissionCommand
import com.compose.presentation.intents.NotificationPermissionCommand.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val getCachedUserUseCase: GetCachedUserUseCase) :
    ViewModel() {

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
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
        var bundle: Bundle?
        var destination: Int
        viewModelScope.launch {
            getCachedUserUseCase().collect { cachedUser ->
                if (cachedUser != null) {
                    destination = R.id.homeFragment
                    bundle = Bundle().apply {
                        putSerializable("homeUser", cachedUser)
                    }

                } else {
                    destination = R.id.loginFragment
                    bundle = null
                }
                _navigationEvent.emit(NavigationEvent.To(destination, bundle))
            }
        }
    }
}