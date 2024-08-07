package com.compose.presentation.mappers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.compose.domain.entities.Message
import com.compose.presentation.R
import com.compose.presentation.models.MessageUiModel
import javax.inject.Inject

class MessageUiModelMapper @Inject constructor(){
    operator fun invoke(message: Message, isCurrentUser:Boolean , dateFormatter:(Long)->String): MessageUiModel {
        val backgroundColor = if (isCurrentUser) R.color.blue else R.color.white
        val arrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start
        val shape = if (isCurrentUser) {
            RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
        } else {
            RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
        }
        return MessageUiModel(
            message = message.message,
            date = dateFormatter(message.timestamp),
            backgroundColor=backgroundColor,
            arrangement = arrangement,
            shape = shape
        )
    }
}