package com.compose.presentation.mappers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.compose.domain.entities.Message
import com.compose.presentation.R
import com.compose.presentation.models.MessageUiModel

fun Message.toUiModel(
    isCurrentUser: Boolean,
    dateFormatter: (Long) -> String
): MessageUiModel {
    val backgroundColor = if (isCurrentUser) R.color.blue else R.color.white
    val arrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
    val shape = if (isCurrentUser) {
        RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
    } else {
        RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
    }
    return MessageUiModel(
        message = this.message,
        date = dateFormatter(this.timestamp),
        backgroundColor = backgroundColor,
        arrangement = arrangement,
        shape = shape
    )
}
fun List<Message>.toUiModel(awayUserId:String,dateFormatter: (Long) -> String):List<MessageUiModel>{
    val messagesUiModelList = this.map { message ->
        val isHomeUser = awayUserId != message.senderId
        message.toUiModel(isHomeUser, dateFormatter)
    }
    return messagesUiModelList
}

