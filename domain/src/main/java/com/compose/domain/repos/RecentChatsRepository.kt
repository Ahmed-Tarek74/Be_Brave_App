package com.compose.domain.repos

import com.compose.domain.entities.Message
import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import kotlinx.coroutines.flow.Flow

interface RecentChatsRepository {
    fun getRecentChats(userId: String): Flow<Result<List<RecentChat>>>
    suspend fun updateRecentChats(homeUserId: String, awayUser: User, message: String)
}