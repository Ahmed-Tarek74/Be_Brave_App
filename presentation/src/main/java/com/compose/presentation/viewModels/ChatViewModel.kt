package com.compose.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.Message
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.SendMessageUseCase
import com.compose.domain.usecases.GetRecentMessagesUseCase
import com.compose.domain.usecases.SendNotificationUseCase
import com.compose.presentation.R
import com.compose.presentation.base.BaseViewModel
import com.compose.presentation.events.ChattingEvent
import com.compose.presentation.intents.ChatIntent
import com.compose.presentation.intents.ChatIntent.*
import com.compose.presentation.mappers.mapToUser
import com.compose.presentation.mappers.toUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewStates.ChatViewState
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
class ChatViewModel @Inject constructor(
    private val sendMessageUseCase: SendMessageUseCase,
    private val getRecentMessagesUseCase: GetRecentMessagesUseCase,
    private val dateFormatterUseCase: DateFormatterUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    private val _viewState = MutableStateFlow(ChatViewState())
    val viewState: StateFlow<ChatViewState> = _viewState
    private val _intent = MutableSharedFlow<ChatIntent>()
    private val _event = MutableSharedFlow<ChattingEvent>()
    val event: SharedFlow<ChattingEvent> = _event
    private val awayUser: UserUiModel = savedStateHandle["awayUser"]!!
    private val homeUser: UserUiModel = savedStateHandle["homeUser"]!!

    init {
        fetchRecentMessages()
        processIntent()
    }

    private fun processIntent() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _intent.collectLatest { intent ->
                when (intent) {
                    is BackToHome -> _event.emit(ChattingEvent.BackToHome(homeUser))
                    is MessageInputChanged -> onMessageInputChanged(intent.messageInput)
                    is SendMessage -> sendMessage(intent.message)
                }
            }
        }
    }

    fun setIntent(intent: ChatIntent) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _intent.emit(intent)
        }
    }

    private fun onMessageInputChanged(message: String) {
        if (message.isNotEmpty())
            _viewState.value = _viewState.value.copy(
                message = message,
                isSendEnabled = true,
                sendBtnContainerColor = R.color.primary_dark_blue
            )
        else
            _viewState.value = _viewState.value.copy(
                message = message,
                isSendEnabled = false,
                sendBtnContainerColor = R.color.gray1
            )
    }

    private suspend fun sendMessage(message: String) {
        if (_viewState.value.isSendEnabled) {
            _viewState.value = _viewState.value.copy(
                message = "",
                isSendEnabled = false,
                sendBtnContainerColor = R.color.gray1
            )
            val senderMessage = Message(
                senderId = homeUser.userId,
                receiverId = awayUser.userId,
                message = message
            )
            try {
                sendMessageUseCase(
                    message = senderMessage,
                    homeUser = homeUser.mapToUser(),
                    awayUser = awayUser.mapToUser()
                )
            } catch (e: Exception) {
                _viewState.value =
                    _viewState.value.copy(errorMsg = e.message ?: "Failed to send Message")
            }
            try {
                sendNotificationUseCase(
                    homeUser.mapToUser(),
                    senderMessage
                )
            } catch (e: Exception) {
                _viewState.value =
                    _viewState.value.copy(errorMsg = "Failed to send Notification")
            }
        }
    }

    private fun formatDate(date: Long): String {
        return dateFormatterUseCase(date)
    }

    private fun fetchRecentMessages() {
        _viewState.value = _viewState.value.copy(
            isLoading = true,
            errorMsg = null
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getRecentMessagesUseCase(homeUser.userId, awayUser.userId)
                    .collectLatest { messagesList ->
                        _viewState.value = _viewState.value.copy(
                            messagesList = (messagesList.toUiModel(
                                awayUser.userId,
                                this@ChatViewModel::formatDate
                            )),
                            isLoading = false,
                            errorMsg = null
                        )
                    }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    errorMsg = e.message ?: "Failed To Load Recent Messages",
                    isLoading = false
                )
            }
        }
    }
}
