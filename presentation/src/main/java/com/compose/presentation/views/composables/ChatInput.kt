package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.compose.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatInput(
    message: String,
    onSendMessage: (String) -> Unit,
    onMessageChanged: (String) -> Unit,
    sendBtnContainerColor: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.gray))
            .padding(3.dp)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChanged,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .weight(1f)
                .padding(end = 3.dp)
            ,
            placeholder = {
                Text(text = stringResource(id = R.string.typeMessage))
            },
            shape = CircleShape,
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                containerColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text
            ),
            maxLines = 1
        )
        FloatingActionButton(
            containerColor = colorResource(id = sendBtnContainerColor),
            contentColor = colorResource(id = R.color.white),
            shape = CircleShape,
            modifier = Modifier.padding(horizontal = 3.dp),
            onClick = {
                onSendMessage(message)
            })
        {
            Icon(
                Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(id = R.string.sendIcon),
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
            )
        }
    }
}