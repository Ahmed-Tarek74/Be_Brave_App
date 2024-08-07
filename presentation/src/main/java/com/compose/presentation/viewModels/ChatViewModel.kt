package com.compose.presentation.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.domain.entities.Message
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.SendMessageUseCase
import com.compose.domain.usecases.GetRecentMessagesUseCase
import com.compose.domain.usecases.SendNotificationUseCase
import com.compose.presentation.R
import com.compose.presentation.events.ChattingEvent
import com.compose.presentation.intents.ChatIntent
import com.compose.presentation.intents.ChatIntent.*
import com.compose.presentation.mappers.MessageUiModelMapper
import com.compose.presentation.mappers.UserUiModelMapper
import com.compose.presentation.models.MessageUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.viewStates.ChatViewState
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val messageUiModelMapper: MessageUiModelMapper,
    private val userUiModelMapper: UserUiModelMapper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
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
        viewModelScope.launch {
            _intent.collectLatest { intent ->
                when (intent) {
                    is ChatIntent.BackToHome -> _event.emit(ChattingEvent.BackToHome(homeUser))
                    is MessageInputChanged -> onMessageInputChanged(intent.messageInput)
                    is SendMessage -> sendMessage(intent.message)
                }
            }
        }
    }

    fun setIntent(intent: ChatIntent) {
        viewModelScope.launch {
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
            try {
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
                sendMessageUseCase(
                    message = senderMessage,
                    homeUser = userUiModelMapper.mapToUserDomainModel(homeUser),
                    awayUser = userUiModelMapper.mapToUserDomainModel(awayUser)
                )
                sendNotificationUseCase(
                    userUiModelMapper.mapToUserDomainModel(homeUser),
                    senderMessage
                )
            } catch (e: Exception) {
                _viewState.value =
                    _viewState.value.copy(errorMsg = e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun formatDate(date: Long): String {
        return dateFormatterUseCase(date)
    }

    private fun fetchRecentMessages() {
        _viewState.value = _viewState.value.copy(
            isLoading = true,
            errorMsg = ""
        )
        viewModelScope.launch {
            try {
                getRecentMessagesUseCase(homeUser.userId, awayUser.userId)
                    .collectLatest { result ->

                        _viewState.value = _viewState.value.copy(
                            messagesList = map(result.getOrElse { emptyList() }),
                            isLoading = false,
                            errorMsg = result.exceptionOrNull()?.message ?: ""
                        )
                    }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    errorMsg = e.message ?: "Failed to fetch recent messages",
                    isLoading = false
                )
            }
        }
    }

    private fun map(messagesList: List<Message>): List<MessageUiModel> {
        val messagesUiModelList = messagesList.map { message ->
            val isHomeUser = awayUser.userId != message.senderId
            messageUiModelMapper(message, isHomeUser, this::formatDate)
        }
        return messagesUiModelList
    }
}