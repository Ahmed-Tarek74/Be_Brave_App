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
import com.compose.domain.entities.User

@Composable
fun UserItemCard(user: User, onUserSelected: (User) -> Unit) {
    Box(
        modifier = Modifier
            .background(color = colorResource(R.color.white))
            .clickable { onUserSelected(user) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    color = colorResource(id = R.color.primary_dark_blue),
                    width = 1.dp,
                    shape = RectangleShape
                )
                .padding(10.dp)
               ,
            verticalAlignment = Alignment.CenterVertically,

            ) {
            AsyncImage(
                model = user.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture },
                contentDescription = "${user.username}'s profile picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color = colorResource(id = R.color.blue)),
                
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start)
            {
                Text(
                    text = user.username, color = colorResource(id = R.color.black),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

        }
    }

}