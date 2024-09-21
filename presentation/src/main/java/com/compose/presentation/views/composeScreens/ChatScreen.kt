package com.compose.presentation.views.composeScreens

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
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.compose.presentation.ui.theme.light_gray
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white

@Composable
fun ChatScreen(
    awayUser: UserUiModel,
    setIntent: (ChatIntent) -> Unit,
    viewState: State<ChatViewState>,
) {
    Column(modifier = Modifier.background(colorScheme.light_gray)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.primary_dark_blue)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { setIntent(ChatIntent.BackToHome) }) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrowBack),
                    tint = colorScheme.white
                )
            }
            AsyncImage(
                model = awayUser.profilePicture,
                contentDescription = stringResource(
                    R.string.profile_picture_description,
                    awayUser.username
                ),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(colorScheme.white),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start)
            {
                Text(
                    text = awayUser.username,
                    style = MaterialTheme.typography.headlineMedium.copy(color = colorScheme.white)
                )
            }
        }
        when {
            viewState.value.isLoading -> {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    color = colorScheme.primary_dark_blue,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            viewState.value.messagesList.isEmpty() -> {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.noRecentMessages),
                    letterSpacing = 2.sp,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
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