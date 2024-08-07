package com.compose.presentation.mappers

import com.compose.domain.entities.RecentChat
import com.compose.presentation.R
import com.compose.presentation.models.RecentChatUiModel
import com.compose.presentation.models.UserUiModel
import javax.inject.Inject

class RecentChatUiMapper @Inject constructor(){
    fun mapToRecentChatUiModel(
        recentChat: RecentChat,
        dateFormatter: (Long) -> String
    ): RecentChatUiModel {
        return RecentChatUiModel(
            awayUser = UserUiModel(userId = recentChat.awayUser.userId,
                username = recentChat.awayUser.username,
                profilePicture = recentChat.awayUser.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture }),
            recentMessage = recentChat.recentMessage,
            timestamp = dateFormatter(recentChat.timestamp)
        )
    }
}