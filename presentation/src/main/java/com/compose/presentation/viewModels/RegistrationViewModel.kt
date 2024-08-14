package com.compose.presentation.viewModels

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.User
import com.compose.domain.usecases.RegistrationUseCase
import com.compose.presentation.R
import com.compose.presentation.events.RegistrationEvent
import com.compose.presentation.events.RegistrationEvent.*
import com.compose.presentation.intents.RegistrationIntent
import com.compose.presentation.intents.RegistrationIntent.*
import com.compose.presentation.viewStates.RegistrationViewState
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

    private val _intent = MutableSharedFlow<RegistrationIntent>()

    private val _event = MutableSharedFlow<RegistrationEvent>()
    val event: SharedFlow<RegistrationEvent> = _event

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
        updatePasswordVisibilityState(isPasswordVisible = _state.value.isPasswordVisible)
    }


    private fun onConfirmationPasswordVisibilityChanged(visibilityState: Boolean) {
        _state.value =
            _state.value.copy(isConfirmationPasswordVisible = !visibilityState)
        updatePasswordVisibilityState(
            isPasswordVisible = _state.value.isConfirmationPasswordVisible,
            isConfirmationPassword = true
        )
    }

    private fun isFormValid(): Boolean {
        return _state.value.username.isNotEmpty() && _state.value.password.isNotEmpty()
                && _state.value.confirmationPassword.isNotEmpty() && _state.value.email.isNotEmpty()
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->

                when (intent) {
                    is Register -> register()
                    is NavigateToLogin -> {
                        _event.emit(backToLogin)
                    }

                    is ConfirmationPasswordChanged -> onConfirmationPasswordChanged(intent.confirmationPassword)
                    is ConfirmationPasswordVisibilityChanged -> onConfirmationPasswordVisibilityChanged(
                        intent.currentVisibility
                    )

                    is EmailChanged -> onEmailChanged(intent.email)
                    is PasswordChanged -> onPasswordChanged(intent.password)
                    is PasswordVisibilityChanged -> onPasswordVisibilityChanged(intent.currentVisibility)
                    is UsernameChanged -> onUsernameChanged(intent.username)
                }
            }
        }
    }

    fun setIntent(intent: RegistrationIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
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
                registrationUseCase(user, _state.value.confirmationPassword)
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(isSuccess = true)
                _event.emit(RegisterSuccessfully)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMessage = e.message ?: "Failed to register",
                    isLoading = false,
                    isSuccess = false
                )
            }
        }
    }

    private fun updatePasswordVisibilityState(
        isPasswordVisible: Boolean,
        isConfirmationPassword: Boolean = false
    ) {
        if (!isConfirmationPassword) {
            _state.value = _state.value.copy(
                passwordVisualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                passwordTrailingIcon = if (isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
                passwordIconDescription = if (isPasswordVisible) R.string.show_password else R.string.hide_password
            )
        } else {
            _state.value = _state.value.copy(
                confirmationPasswordVisualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                confirmationPasswordTrailingIcon = if (isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
                confirmationPasswordIconDescription = if (isPasswordVisible) R.string.show_password else R.string.hide_password
            )
        }
    }
}