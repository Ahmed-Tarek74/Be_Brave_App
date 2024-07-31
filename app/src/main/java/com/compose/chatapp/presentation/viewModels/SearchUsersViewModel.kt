package com.compose.chatapp.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.chatapp.presentation.events.NavigationEvent
import com.compose.chatapp.presentation.events.NavigationEvent.*
import com.compose.chatapp.presentation.intents.SearchUsersIntent
import com.compose.chatapp.presentation.intents.SearchUsersIntent.*
import com.compose.chatapp.presentation.viewStates.SearchUsersViewState
import com.compose.domain.entities.User
import com.compose.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUsersViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {
    private val _viewState = MutableStateFlow(SearchUsersViewState())
    val viewState: StateFlow<SearchUsersViewState> = _viewState
    private val _event = MutableSharedFlow<NavigationEvent>()
    val event: SharedFlow<NavigationEvent> = _event
    private val _intent = MutableStateFlow<SearchUsersIntent?>(null)
    private var homeUser: User = savedStateHandle["homeUser"]!!

    init {
        processIntents()
    }

    private fun onQueryChanged(query: String) {
        _viewState.value = _viewState.value.copy(searchQuery = query)
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->
                intent?.let {
                    when (it) {
                        is UpdateSearchQuery -> {
                            onQueryChanged(it.searchQuery)
                            searchUsers(it.searchQuery,homeUser.userId)
                        }
                        is SelectUser -> {
                            _event.emit(NavigateToChattingScreen(homeUser = homeUser, awayUser = it.user))
                        }
                        is BackToHome -> _event.emit(NavigateToHome(homeUser))
                    }
                }
            }
        }
    }
    fun setIntent(intent: SearchUsersIntent) {
        viewModelScope.launch {
            _intent.value = intent
        }
    }

    private suspend fun searchUsers(query: String, userId: String) {
        _viewState.value =
            _viewState.value.copy(isLoading = true, errorMsg = null, emptyListErrorMsg = null)
        getUsersUseCase.searchUsers(query,userId).collect { result ->
            _viewState.value = _viewState.value.copy(isLoading = false)
            if (result.isSuccess) {
                if (result.getOrNull().isNullOrEmpty()) {
                    _viewState.value = _viewState.value.copy(emptyListErrorMsg = query)
                } else {
                    _viewState.value = _viewState.value.copy(usersList = result.getOrNull()!!)
                }
            } else {
                _viewState.value =
                    _viewState.value.copy(errorMsg = result.exceptionOrNull()?.message.toString())
            }
        }
    }
}