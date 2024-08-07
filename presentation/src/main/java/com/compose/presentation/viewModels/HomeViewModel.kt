package com.compose.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.RecentChat
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.GetRecentChatsUseCase
import com.compose.domain.usecases.LogOutUseCase
import com.compose.presentation.events.HomeEvent
import com.compose.presentation.events.HomeEvent.*
import com.compose.presentation.intents.HomeIntent
import com.compose.presentation.intents.HomeIntent.*
import com.compose.presentation.mappers.RecentChatUiMapper
import com.compose.presentation.models.RecentChatUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewStates.HomeViewState
import com.compose.presentation.viewStates.HomeViewState.*
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val recentChatMapper: RecentChatUiMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _intent = MutableSharedFlow<HomeIntent>()
    private val _viewState = MutableStateFlow<HomeViewState>(Loading)
    private val _event = MutableSharedFlow<HomeEvent>()

    val viewState: StateFlow<HomeViewState> = _viewState
    val event: SharedFlow<HomeEvent> = _event

    private var homeUser: UserUiModel? = savedStateHandle["homeUser"]!!

    init {
        viewModelScope.launch {
            loadRecentChats()
        }
        processIntents()
    }

    fun setIntent(intent: HomeIntent) {
        viewModelScope.launch {
            _intent.emit(intent)

        }
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->
                handleIntent(intent)
            }
        }
    }

    private fun handleIntent(intent: HomeIntent) {
        viewModelScope.launch {
            when (intent) {
                is SelectRecentChat -> _event.emit(
                    NavigateToChattingScreen(
                        homeUser = homeUser!!,
                        awayUser = intent.selectedUser
                    )
                )
                is StartNewChat -> _event.emit(NavigateToSearchScreen(homeUser!!))
                is LoggedOut -> {
                    logout()
                }
            }
        }
    }
    private suspend fun loadRecentChats() {
        _viewState.value = Loading
        try {
            getRecentChatsUseCase(homeUser!!.userId).collectLatest { result ->
                _viewState.value = if (result.isSuccess) {

                    Success(map(result.getOrNull()!!))
                } else {
                    Failure(result.exceptionOrNull()?.message ?: "Unknown error occurred")
                }
            }
        } catch (e: Exception) {
            _viewState.value = Failure(e.message ?: "Error fetching recent chats")
        }
    }

    fun formatDate(date: Long): String {
        return dateFormatterUseCase(date)
    }

    private suspend fun logout() {
        try {
            logOutUseCase()
            _event.emit(NavigateToLoginScreen)
        } catch (e: Exception) {
            _viewState.value = Failure(e.message ?: "Logout failed")
        }

    }
    private fun map(recentChatsList: List<RecentChat>): List<RecentChatUiModel> {
        val recentChatsUiModelList = recentChatsList.map { recentChat ->
            recentChatMapper.mapToRecentChatUiModel(recentChat, this::formatDate)
        }
        return recentChatsUiModelList
    }
}
