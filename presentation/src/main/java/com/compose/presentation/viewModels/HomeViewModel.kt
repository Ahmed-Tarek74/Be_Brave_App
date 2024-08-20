package com.compose.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.GetRecentChatsUseCase
import com.compose.domain.usecases.LogOutUseCase
import com.compose.presentation.events.HomeEvent
import com.compose.presentation.events.HomeEvent.*
import com.compose.presentation.intents.HomeIntent
import com.compose.presentation.intents.HomeIntent.*
import com.compose.presentation.mappers.toUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewStates.HomeViewState
import com.compose.presentation.viewStates.HomeViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
    private val getRecentChatsUseCase: GetRecentChatsUseCase,
    private val dateFormatterUseCase: DateFormatterUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _intent = MutableSharedFlow<HomeIntent>()
    private val _viewState = MutableStateFlow<HomeViewState>(Loading)
    private val _event = MutableSharedFlow<HomeEvent>()

    val viewState: StateFlow<HomeViewState> = _viewState
    val event: SharedFlow<HomeEvent> = _event

    private var homeUser: UserUiModel? = savedStateHandle["homeUser"]!!

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _viewState.value = Failure(exception.message ?: "An unexpected error occurred")
    }

    init {
        loadRecentChats()
        processIntents()
    }

    fun setIntent(intent: HomeIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private fun processIntents() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _intent.collectLatest { intent ->
                handleIntent(intent)
            }
        }
    }

    private suspend fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is SelectRecentChat -> _event.emit(
                ChatSelected(
                    homeUser = homeUser!!,
                    awayUser = intent.selectedUser
                )
            )

            is StartNewChat -> _event.emit(NavigateToSearchScreen(homeUser!!))
            is HomeIntent.LoggedOut -> {
                logout()
            }
        }
    }

    private fun loadRecentChats() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _viewState.value = Loading
            getRecentChatsUseCase(homeUser!!.userId).collectLatest { recentChats ->
                _viewState.value = Success(recentChats.toUiModel(this@HomeViewModel::formatDate))
            }
        }
    }

    private fun logout() {
        viewModelScope.launch(coroutineExceptionHandler) {
            logOutUseCase()
            _event.emit(HomeEvent.LoggedOut)
        }
    }

    private fun formatDate(date: Long): String {
        return dateFormatterUseCase(date)
    }
}
