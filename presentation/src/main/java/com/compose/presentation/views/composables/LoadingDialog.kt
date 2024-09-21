package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.compose.presentation.R
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white

@Composable
fun LoadingDialog(loadingMsg: String, onDismiss: () -> Unit = {}) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.white)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary_dark_blue)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = loadingMsg, style = MaterialTheme.typography.headlineLarge)
            }
        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview() {
    LoadingDialog(loadingMsg = stringResource(id = R.string.signingIn))
}
