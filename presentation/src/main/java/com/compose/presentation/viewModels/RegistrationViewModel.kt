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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
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
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    init {
        processIntents()
    }

    private fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun updateEmail(email: String) {
        _state.value = _state.value.copy(email = email)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun updateConfirmationPassword(confirmationPassword: String) {
        _state.value = _state.value.copy(confirmationPassword = confirmationPassword)
        _state.value = _state.value.copy(isRegisterEnabled = isFormValid())
    }

    private fun updatePasswordVisibility(visibilityState: Boolean) {
        _state.value =
            _state.value.copy(isPasswordVisible = !visibilityState)
        updatePasswordVisibilityState(isPasswordVisible = _state.value.isPasswordVisible)
    }


    private fun updateConfirmationPasswordVisibility(visibilityState: Boolean) {
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

    private fun processIntents() =
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _intent.collectLatest { intent ->
                when (intent) {
                    is Register -> register()
                    is NavigateToLogin -> {
                        _event.emit(backToLogin)
                    }

                    is ConfirmationPasswordChanged -> updateConfirmationPassword(intent.confirmationPassword)
                    is ConfirmationPasswordVisibilityChanged -> updateConfirmationPasswordVisibility(
                        intent.currentVisibility
                    )

                    is EmailChanged -> updateEmail(intent.email)
                    is PasswordChanged -> updatePassword(intent.password)
                    is PasswordVisibilityChanged -> updatePasswordVisibility(intent.currentVisibility)
                    is UsernameChanged -> updateUsername(intent.username)
                }
            }
        }

    fun setIntent(intent: RegistrationIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private suspend fun register() {
        try {
            val user = User(
                username = _state.value.username,
                password = _state.value.password,
                email = _state.value.email
            )
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)
            registrationUseCase(user, _state.value.confirmationPassword)
            _state.value = _state.value.copy(isLoading = false)
            _event.emit(RegisterSuccessfully)
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                errorMessage = e.message ?: "Failed to register",
                isLoading = false,
            )
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
    private fun handleException(exception: Throwable) {
        _state.value = _state.value.copy(
            errorMessage = exception.message ?: "An unexpected error occurred. Please try again.",
            isLoading = false
        )
    }
}