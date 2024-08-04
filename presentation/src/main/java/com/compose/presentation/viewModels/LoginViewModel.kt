package com.compose.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.User
import com.compose.domain.usecases.LoginUseCase
import com.compose.presentation.events.NavigationEvent
import com.compose.presentation.intents.LoginIntent
import com.compose.presentation.intents.LoginIntent.*
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
) : ViewModel() {

    private val _intent = MutableStateFlow<LoginIntent?>(null)

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState

    private val _event = MutableSharedFlow<NavigationEvent>()
    val event: SharedFlow<NavigationEvent> = _event

    init {
        processIntents()
    }

    private fun onEmailChanged(email: String) {
        _viewState.value = _viewState.value.copy(email = email)
        _viewState.value = _viewState.value.copy(isLoginEnabled = isFormValid())
    }

    private fun onPasswordChanged(password: String) {
        _viewState.value = _viewState.value.copy(password = password)
        _viewState.value = _viewState.value.copy(isLoginEnabled = isFormValid())
    }

    private fun onPasswordVisibilityChanged(visibilityState: Boolean) {
        _viewState.value =
            _viewState.value.copy(isPasswordVisible = !visibilityState)
    }

    private fun isFormValid(): Boolean {
        return _viewState.value.email.isNotEmpty() && _viewState.value.password.isNotEmpty()
    }

    fun setIntent(intent: LoginIntent) {
        _intent.value = intent
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->
                intent?.let {
                    when (it) {
                        is Login -> login(it.user)
                        is EmailChanged -> onEmailChanged(it.email)
                        is PasswordChanged -> onPasswordChanged(it.password)
                        is PasswordVisibilityChanged -> onPasswordVisibilityChanged(it.currentVisibility)
                        is NavigateToRegister -> _event.emit(NavigationEvent.NavigateToRegistration)
                    }
                }
            }
        }
    }
    private suspend fun login(userCredentials: User) {
        _viewState.value = _viewState.value.copy(isLoading = true, errorMessage = null)
        try {
            loginUseCase(userCredentials).collect { result ->
                _viewState.value = _viewState.value.copy(isLoading = false)
                if (result.isSuccess) {
                    val navigateToHome=NavigationEvent.NavigateToHome(result.getOrNull()!!)
                    _event.emit(navigateToHome)
                } else {
                    _viewState.value =
                        _viewState.value.copy(errorMessage = result.exceptionOrNull()?.message.toString())
                    _intent.value = null
                }
            }
        } catch (e: Exception) {
            _viewState.value =
                _viewState.value.copy(errorMessage = e.message!!, isLoading = false)
        }
    }
}
