package com.compose.chatapp.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.chatapp.presentation.events.NavigationEvent
import com.compose.chatapp.presentation.intents.RegistrationIntent
import com.compose.chatapp.presentation.intents.RegistrationIntent.*
import com.compose.chatapp.presentation.viewStates.RegistrationViewState
import com.compose.domain.entities.User
import com.compose.domain.usecases.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationViewState())
    val state: StateFlow<RegistrationViewState> = _state

    private val _intent = MutableStateFlow<RegistrationIntent?>(null)

    private val _event = MutableSharedFlow<NavigationEvent>()
    val event: SharedFlow<NavigationEvent> = _event

    init {
        processIntents()
    }

    private fun onUsernameChanged(username: String) {
        _state.value = _state.value.copy(username = username)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun onPasswordChanged(password: String) {
        _state.value = _state.value.copy(password = password)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun onConfirmationPasswordChanged(confirmationPassword: String) {
        _state.value = _state.value.copy(confirmationPassword = confirmationPassword)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun onPasswordVisibilityChanged(visibilityState: Boolean) {
        _state.value =
            _state.value.copy(isPasswordVisible = !visibilityState)
    }


    private fun onConfirmationPasswordVisibilityChanged(visibilityState: Boolean) {
        _state.value =
            _state.value.copy(isConfirmationPasswordVisible = !visibilityState)
    }
    private fun isFormValid(): Boolean {
        return _state.value.username.isNotEmpty() && _state.value.password.isNotEmpty()
                && _state.value.confirmationPassword.isNotEmpty() && _state.value.email.isNotEmpty()
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->
                intent?.let {
                    when (it) {
                        is Register -> register()
                        is NavigateToLogin -> {
                            _event.emit(NavigationEvent.NavigateToLoginScreen)
                        }
                        is ConfirmationPasswordChanged -> onConfirmationPasswordChanged(it.confirmationPassword)
                        is ConfirmationPasswordVisibilityChanged -> onConfirmationPasswordVisibilityChanged(
                            it.currentVisibility
                        )

                        is EmailChanged -> onEmailChanged(it.email)
                        is PasswordChanged -> onPasswordChanged(it.password)
                        is PasswordVisibilityChanged -> onPasswordVisibilityChanged(it.currentVisibility)
                        is UsernameChanged -> onUsernameChanged(it.username)
                    }
                }
            }
        }
    }

    fun setIntent(intent: RegistrationIntent) {
        _intent.value = intent
    }

    private fun register() {
        val user = User(
            username = _state.value.username,
            password = _state.value.password,
            email = _state.value.email
        )
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {

                registrationUseCase(user,_state.value.confirmationPassword).collect { result ->
                    _state.value = _state.value.copy(isLoading = false)
                    if (result.isSuccess) {
                        _state.value = _state.value.copy(isSuccess = true)
                        val navigateToLogin = NavigationEvent.NavigateToLoginScreen
                        _event.emit(navigateToLogin)
                    } else {
                        _state.value = _state.value.copy(
                            errorMessage = result.exceptionOrNull()!!.message
                                ?: "Failed to register",
                            isSuccess = false
                        )
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = e.message ?: "Failed to register",
                    isLoading = false,
                    isSuccess = false
                )
            }
        }
    }
}