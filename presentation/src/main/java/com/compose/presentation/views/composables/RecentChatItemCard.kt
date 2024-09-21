package com.compose.presentation.views.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.presentation.R
import com.compose.presentation.models.RecentChatUiModel
import com.compose.presentation.models.UserUiModel
import com.compose.presentation.ui.theme.SEMI_BOLD
import com.compose.presentation.ui.theme.dark_gray
import com.compose.presentation.ui.theme.light_blue
import com.compose.presentation.ui.theme.primary_dark_blue
import com.compose.presentation.ui.theme.white

@Composable
fun RecentChatCard(
    recentChat: RecentChatUiModel,
    onChatSelected: (UserUiModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .background(colorScheme.white)
            .clickable { onChatSelected(recentChat.awayUser) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = colorScheme.primary_dark_blue,
                    width = 1.dp,
                    shape = RectangleShape
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            ) {
            AsyncImage(
                model = recentChat.awayUser.profilePicture,
                contentDescription = stringResource(
                    R.string.user_profile_picture,
                    recentChat.awayUser.username
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(colorScheme.light_blue),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            {
                Text(
                    text = recentChat.awayUser.username,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = recentChat.recentMessage,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontFamily = FontFamily.SEMI_BOLD,
                        color = colorScheme.dark_gray
                    ),
                    maxLines = 1
                )
            }
            Text(
                text = recentChat.timestamp,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.dark_gray
                ),
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(vertical = 8.dp)

            )

        }
    }

}