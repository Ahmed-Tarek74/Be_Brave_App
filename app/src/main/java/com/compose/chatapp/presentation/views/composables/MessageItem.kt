package com.compose.chatapp.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.chatapp.R
import com.compose.domain.entities.Message

@Composable
fun ChatMessageItem(
    message: Message, isCurrentUser: Boolean,
    dateFormatter: (Long)->String,
) {
    val backgroundColor =
        if (isCurrentUser) (colorResource(id = R.color.blue)) else colorResource(
            id = R.color.white
        )
    val arrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start

    val shape = if (isCurrentUser) {
        RoundedCornerShape(8.dp, 0.dp, 8.dp, 8.dp)
    } else {
        RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)
    }
    Row(
        horizontalArrangement = arrangement, modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .background(backgroundColor, shape)
                .padding(8.dp)
        ) {
            Text(
                text = message.message,
                color = colorResource(id = R.color.black),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = dateFormatter(message.timestamp),
                fontSize = 10.sp,
                color = colorResource(id = R.color.gray2),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
