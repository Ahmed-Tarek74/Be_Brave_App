package com.compose.presentation.views.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.compose.presentation.R

@Composable
fun ErrorDialog(errorMsg:String,onDismiss: () -> Unit) {
    AlertDialog(
        shape = RoundedCornerShape(20.dp),
        containerColor = colorResource(id = R.color.white),
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.error), style=MaterialTheme.typography.headlineSmall)
        },
        text = {
            Text(text = errorMsg,style=MaterialTheme.typography.bodySmall)
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = "OK")
            }
        }
    )
}