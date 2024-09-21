package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.compose.presentation.models.MessageUiModel
import com.compose.presentation.ui.theme.dark_gray

@Composable
fun ChatMessageItem(message: MessageUiModel) {
    Row(
        horizontalArrangement = message.arrangement, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
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
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            Text(
                text = message.date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.dark_gray
                ),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
