package com.compose.chatapp.presentation.views.composables

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.compose.chatapp.R
import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User

@Composable
fun RecentChatCard(
    recentChat: RecentChat,
    dateFormatter: (Long)->String,
    onChatSelected: (User) -> Unit
) {
    Box(
        modifier = Modifier
            .background(color = colorResource(R.color.white))
            .clickable { onChatSelected(recentChat.awayUser) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = colorResource(id = R.color.primary_dark_blue),
                    width = 1.dp,
                    shape = RectangleShape
                )
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,

            ) {
            AsyncImage(
                model = recentChat.awayUser.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture },
                contentDescription = "${recentChat.awayUser.username}'s profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.gray)),

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
                    text = recentChat.awayUser.username, color = colorResource(id = R.color.black),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = recentChat.recentMessage,
                    color = colorResource(id = R.color.gray2),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1
                )
            }
            Text(
                text = dateFormatter(recentChat.timestamp),
                color = colorResource(id = R.color.gray2),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(vertical = 10.dp)
            )

        }
    }

}