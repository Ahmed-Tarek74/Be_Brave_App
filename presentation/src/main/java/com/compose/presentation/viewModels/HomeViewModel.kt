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
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.value = Loading
            try {
                getRecentChatsUseCase(homeUser!!.userId).collectLatest { recentChats ->
                    _viewState.value =
                        Success(recentChats.toUiModel(this@HomeViewModel::formatDate))
                }
            } catch (e: Exception) {
                _viewState.value = Failure(e.message ?: "Failed to load recent chats")
            }
        }
    }

    private suspend fun logout() {
        try {
            logOutUseCase()
            _event.emit(HomeEvent.LoggedOut)
        } catch (e: Exception) {
            _viewState.value = Failure(e.message ?: "Failed to logout")
        }
    }
    private fun formatDate(date: Long): String {
        return dateFormatterUseCase(date)
    }
}
