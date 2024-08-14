package com.compose.presentation.mappers

import com.compose.domain.entities.RecentChat
import com.compose.presentation.R
import com.compose.presentation.models.RecentChatUiModel
import com.compose.presentation.models.UserUiModel

fun RecentChat.toRecentChatUiModel(
    dateFormatter: (Long) -> String
): RecentChatUiModel {
    return RecentChatUiModel(
        awayUser = UserUiModel(
            userId = this.awayUser.userId,
            username = this.awayUser.username,
            profilePicture = this.awayUser.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture }),
        recentMessage = this.recentMessage,
        timestamp = dateFormatter(this.timestamp)
    )
}

fun List<RecentChat>.toUiModel(dateFormatter: (Long) -> String): List<RecentChatUiModel> {
    val recentChatsUiModelList = this.map { recentChat ->
        recentChat.toRecentChatUiModel(dateFormatter)
    }
    return recentChatsUiModelList
}
