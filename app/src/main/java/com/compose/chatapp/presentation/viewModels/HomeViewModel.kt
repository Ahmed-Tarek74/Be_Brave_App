package com.compose.chatapp.presentation.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.chatapp.presentation.events.NavigationEvent
import com.compose.chatapp.presentation.events.NavigationEvent.*
import com.compose.chatapp.presentation.intents.HomeIntent
import com.compose.chatapp.presentation.intents.HomeIntent.*
import com.compose.chatapp.presentation.viewStates.HomeViewState
import com.compose.chatapp.presentation.viewStates.HomeViewState.*
import com.compose.domain.entities.User
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.GetRecentChatsUseCase
import com.compose.domain.usecases.LogOutUseCase
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _intent = MutableStateFlow<HomeIntent>(LoadRecentChats)
    private val _viewState = MutableStateFlow<HomeViewState>(Loading)
    private val _event = MutableSharedFlow<NavigationEvent>()

    val viewState: StateFlow<HomeViewState> = _viewState
    val event: SharedFlow<NavigationEvent> = _event

    private var homeUser: User? = savedStateHandle["homeUser"]!!

    init {
        processIntents()
    }

    fun setIntent(intent: HomeIntent) {
        _intent.value = intent
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
                is LoadRecentChats -> loadRecentChats()
                is SelectRecentChat -> _event.emit(NavigateToChattingScreen(homeUser = homeUser!!, awayUser =  intent.selectedUser))
                is StartNewChat -> _event.emit(NavigateToSearchScreen(homeUser!!))
                is LoggedOut -> {
                    performLogout()
                }
            }
        }
    }

    private suspend fun loadRecentChats() {
        _viewState.value = Loading
        try {
            getRecentChatsUseCase(homeUser!!.userId).collectLatest { result ->
                _viewState.value = if (result.isSuccess) {
                    Success(result.getOrNull()!!)
                } else {
                    Failure(result.exceptionOrNull()?.message ?: "Unknown error occurred")
                }
            }
        } catch (e: Exception) {
            _viewState.value = Failure(e.message ?: "Error fetching recent chats")
        }
    }
    fun formatDate(date:Long):String{
        return dateFormatterUseCase(date)
    }
    private suspend fun performLogout() {
            try {
                logOutUseCase()
                _event.emit(NavigateToLoginScreen)
            } catch (e: Exception) {
                _viewState.value = Failure(e.message ?: "Logout failed")
            }

    }
}
