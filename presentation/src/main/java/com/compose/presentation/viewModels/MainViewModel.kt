package com.compose.presentation.viewModels

import androidx.lifecycle.viewModelScope
import com.compose.domain.usecases.GetCachedUserUseCase
import com.compose.presentation.base.BaseViewModel
import com.compose.presentation.events.UserCacheEvent
import com.compose.presentation.events.UserCacheEvent.*
import com.compose.presentation.intents.NotificationPermissionCommand
import com.compose.presentation.intents.NotificationPermissionCommand.*
import com.compose.presentation.mappers.mapToUserUiModel
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
) : BaseViewModel() {

    private val _userLoadedEvent = MutableSharedFlow<UserCacheEvent>()
    val userLoadedEvent = _userLoadedEvent.asSharedFlow()

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
        viewModelScope.launch(coroutineExceptionHandler) {
            try {
                // Retrieve the cached user asynchronously on the IO dispatcher
                val loggedInUser = withContext(Dispatchers.IO) { getCachedUserUseCase() }

                // Emit event based on whether the user was found or not
                val userLoadedEvent = if (loggedInUser != null) {
                    UserLoaded(
                        isUserLoggedIn = true,
                        user = loggedInUser.mapToUserUiModel()
                    )
                } else {
                    UserLoaded(isUserLoggedIn = false)
                }
                _userLoadedEvent.emit(userLoadedEvent)
            } catch (e: Exception) {
                // Emit an error event with a default message if errorMsg is null
                _userLoadedEvent.emit(
                    UserLoadFailed(
                        errorMsg = e.message ?: "Failed to get saved user"
                    )
                )
            }
        }
    }
}