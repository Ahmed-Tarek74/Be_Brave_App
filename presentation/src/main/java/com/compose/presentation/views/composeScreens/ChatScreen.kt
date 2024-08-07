package com.compose.presentation.views.composeScreens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.compose.presentation.R
import com.compose.presentation.intents.ChatIntent
import com.compose.presentation.viewStates.ChatViewState
import com.compose.presentation.views.composables.ChatInput
import com.compose.presentation.views.composables.ChatMessageItem
import com.compose.presentation.models.UserUiModel

@Composable
fun ChatScreen(
    awayUser: UserUiModel,
    setIntent: (ChatIntent) -> Unit,
    viewState: State<ChatViewState>
) {
    Column(modifier = Modifier.background(colorResource(id = R.color.gray))) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.primary_dark_blue))
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { setIntent(ChatIntent.BackToHome) }) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrowBack),
                    tint = colorResource(
                        id = R.color.white
                    )
                )
            }
            AsyncImage(
                model = awayUser.profilePicture,
                contentDescription = stringResource(
                    R.string.profile_picture_description,
                    awayUser.username
                ),
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.white)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start)
            {
                Text(
                    text = awayUser.username, color = colorResource(id = R.color.white),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
        when {
            viewState.value.errorMsg.isNotEmpty() -> {
                Toast.makeText(LocalContext.current, viewState.value.errorMsg, Toast.LENGTH_LONG)
                    .show()
            }

            viewState.value.isLoading -> {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    color = colorResource(id = R.color.primary_dark_blue),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            viewState.value.messagesList.isEmpty() -> {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.noRecentMessages),
                    letterSpacing = 2.sp,
                    color = colorResource(id = R.color.primary_dark_blue),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(viewState.value.messagesList) { message ->
                        ChatMessageItem(message = message)
                    }
                }
            }
        }
        ChatInput(
            message = viewState.value.message,
            onSendMessage = { setIntent(ChatIntent.SendMessage(it)) },
            onMessageChanged = { setIntent(ChatIntent.MessageInputChanged(it)) },
            sendBtnContainerColor = viewState.value.sendBtnContainerColor
        )
    }
}