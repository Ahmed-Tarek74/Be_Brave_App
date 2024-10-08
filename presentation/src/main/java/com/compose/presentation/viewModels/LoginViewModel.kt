package com.compose.presentation.viewModels

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewModelScope
import com.compose.domain.usecases.LoginUseCase
import com.compose.presentation.R
import com.compose.presentation.base.BaseViewModel
import com.compose.presentation.events.LoginEvent
import com.compose.presentation.events.LoginEvent.*
import com.compose.presentation.intents.LoginIntent
import com.compose.presentation.intents.LoginIntent.*
import com.compose.presentation.mappers.mapToUserUiModel
import com.compose.presentation.viewStates.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel() {
    private val _intent = MutableSharedFlow<LoginIntent>()
    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState
    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event

    init {
        processIntents()
    }

    private fun updateEmail(email: String) {
        _viewState.value = _viewState.value.copy(email = email)
        _viewState.value = _viewState.value.copy(isLoginEnabled = isFormValid())
    }

    private fun updatePassword(password: String) {
        _viewState.value = _viewState.value.copy(password = password)
        _viewState.value = _viewState.value.copy(isLoginEnabled = isFormValid())
    }

    private fun updatePasswordVisibilityState(visibilityState: Boolean) {
        _viewState.value =
            _viewState.value.copy(isPasswordVisible = !visibilityState)
        updatePasswordVisibilityState()
    }

    private fun isFormValid(): Boolean {
        return _viewState.value.email.isNotEmpty() && _viewState.value.password.isNotEmpty()
    }

    fun setIntent(intent: LoginIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun processIntents() {
        viewModelScope.launch(coroutineExceptionHandler)
        {
            _intent.collectLatest { intent ->
                when (intent) {
                    is Login -> login()
                    is EmailChanged -> updateEmail(intent.email)
                    is PasswordChanged -> updatePassword(intent.password)
                    is PasswordVisibilityChanged -> updatePasswordVisibilityState(intent.currentVisibility)
                    is NavigateToRegister -> _event.emit(NavigateToRegistration)
                }
            }
        }
    }

    private fun updatePasswordVisibilityState() {
        val isPasswordVisible = _viewState.value.isPasswordVisible
        _viewState.value = _viewState.value.copy(
            passwordVisualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            passwordTrailingIcon = if (isPasswordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24,
            passwordIconDescription = if (isPasswordVisible) R.string.show_password else R.string.hide_password
        )
    }

    private suspend fun login() {
        try {
            _viewState.value = _viewState.value.copy(isLoading = true, errorMessage = null)
            val email = _viewState.value.email
            val password = _viewState.value.password
            val user = loginUseCase(email, password).mapToUserUiModel()
            _viewState.value = _viewState.value.copy(isLoading = false)
            _event.emit(LoginSuccess(user))
        } catch (e: Exception) {
            _viewState.value = _viewState.value.copy(
                errorMessage = e.message ?: "Failed to login",
                isLoading = false
            )
        }
    }
}