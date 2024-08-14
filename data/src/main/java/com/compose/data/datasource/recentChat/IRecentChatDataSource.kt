package com.compose.data.datasource.recentChat

import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface IRecentChatDataSource {
    fun fetchRecentChats(userId: String): Flow<List<RecentChat>>
    suspend fun updateRecentChat(homeUserId: String, awayUser: User, message: String)
}