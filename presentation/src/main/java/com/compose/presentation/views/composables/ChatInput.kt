package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.compose.presentation.R

@Composable
fun ChatInput(
    message: String,
    onSendMessage: (String) -> Unit,
    onMessageChanged: (String) -> Unit,
    isSendEnabled: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(3.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.primary_dark_blue),
                    shape = CircleShape
                )
                .background(color = Color.Transparent, shape = CircleShape)
                .padding(13.dp)
                .weight(1f)

        ) {
            BasicTextField(
                value = message,
                onValueChange = onMessageChanged,
                cursorBrush = SolidColor(Color.Black),
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier.fillMaxWidth()
                    .padding(2.dp),
                decorationBox = { innerTextField ->
                    if (message.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.typeMessage),
                            color = colorResource(id = R.color.gray2)
                        )
                    }
                    innerTextField()
                }
            )
        }
        val containerColor =if (isSendEnabled) (colorResource(id = R.color.primary_dark_blue)) else colorResource(
            id = R.color.gray1
        )
        SmallFloatingActionButton(containerColor =containerColor,
            contentColor = colorResource(id = R.color.white), shape = CircleShape,
            onClick = {
                if (isSendEnabled) {
                    onSendMessage(message)
                }
            })
        {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = stringResource(id = R.string.sendIcon), modifier = Modifier.size(24.dp)
                .padding(2.dp))
        }
    }
}