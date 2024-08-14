package com.compose.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.User
import com.compose.presentation.intents.SearchUsersIntent
import com.compose.presentation.intents.SearchUsersIntent.*
import com.compose.presentation.viewStates.SearchUsersViewState
import com.compose.domain.usecases.GetUsersUseCase
import com.compose.presentation.events.SearchUsersEvent
import com.compose.presentation.mappers.mapToUserUiModel
import com.compose.presentation.models.UserUiModel
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
    private val _event = MutableSharedFlow<SearchUsersEvent>()
    val event: SharedFlow<SearchUsersEvent> = _event
    private val _intent = MutableSharedFlow<SearchUsersIntent>()
    private var homeUser: UserUiModel = savedStateHandle["homeUser"]!!

    init {
        processIntents()
    }

    private fun onQueryChanged(query: String) {
        _viewState.value = _viewState.value.copy(searchQuery = query)
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collectLatest { intent ->

                when (intent) {
                    is UpdateSearchQuery -> {
                        onQueryChanged(intent.searchQuery)
                        searchUsers(intent.searchQuery, homeUser.userId)
                    }

                    is SelectUser -> {
                        _event.emit(
                            SearchUsersEvent.UserSelected(
                                homeUser = homeUser,
                                awayUser = intent.user
                            )
                        )
                    }
                    is BackToHome -> _event.emit(SearchUsersEvent.BackToHome(homeUser))
                }

            }
        }
    }

    fun setIntent(intent: SearchUsersIntent) {
        viewModelScope.launch {
            _intent.emit(intent)
        }
    }

    private suspend fun searchUsers(query: String, userId: String) {
        _viewState.value =
            _viewState.value.copy(isLoading = true, errorMsg = null, emptyListErrorMsg = null)
        try {
            val filteredUsersList = getUsersUseCase.searchUsers(query, userId)
            _viewState.value = _viewState.value.copy(isLoading = false)
            if (filteredUsersList.isEmpty()) {
                _viewState.value = _viewState.value.copy(emptyListErrorMsg = query)
            } else {
                _viewState.value = _viewState.value.copy(usersList = map(filteredUsersList))
            }
        } catch (e: Exception) {
            _viewState.value =
                _viewState.value.copy(errorMsg = e.message ?: "Failed to search for $query", isLoading = false)
        }
    }
    private fun map(userList: List<User>): List<UserUiModel> {
        val usersUiModelList = userList.map { user ->
            user.mapToUserUiModel()
        }
        return usersUiModelList
    }
}