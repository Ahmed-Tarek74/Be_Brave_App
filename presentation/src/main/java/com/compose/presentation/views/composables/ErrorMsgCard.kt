package com.compose.presentation.views.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.compose.presentation.R
import com.compose.presentation.ui.theme.light_red
import com.compose.presentation.ui.theme.pink
import com.compose.presentation.ui.theme.red

@Composable
fun ErrorMsgCard(errorMsg: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.pink
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            width = (1.dp),
            color = colorScheme.light_red
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    )
    {
        Text(
            text = errorMsg,
            style = typography.bodyLarge.copy(color = colorScheme.red),
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorMsgCardPreview() {
    ErrorMsgCard(errorMsg = stringResource(id = R.string.saved_user_failure_msg))
}