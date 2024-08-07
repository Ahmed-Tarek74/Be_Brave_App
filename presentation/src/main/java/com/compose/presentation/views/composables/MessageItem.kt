package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compose.presentation.R
import com.compose.presentation.models.MessageUiModel

@Composable
fun ChatMessageItem(message: MessageUiModel) {
    Row(
        horizontalArrangement = message.arrangement, modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    colorResource(id = message.backgroundColor),
                    shape = message.shape
                )
                .padding(8.dp)
        ) {
            Text(
                text = message.message,
                color = colorResource(id = R.color.black),
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = message.date,
                fontSize = 10.sp,
                color = colorResource(id = R.color.gray2),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
