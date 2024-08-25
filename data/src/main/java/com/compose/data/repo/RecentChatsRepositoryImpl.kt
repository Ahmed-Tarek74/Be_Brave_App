package com.compose.data.repo

import com.compose.domain.entities.RecentChat
import com.compose.domain.entities.User
import com.compose.domain.repos.RecentChatsRepository
import com.compose.data.datasource.recentChat.RecentChatDataSource
import kotlinx.coroutines.flow.Flow

class RecentChatsRepositoryImpl(
    private val recentChatsDataSource: RecentChatDataSource
) : RecentChatsRepository {

    override fun getRecentChats(userId: String): Flow<List<RecentChat>>{
        return try {
            recentChatsDataSource.fetchRecentChats(userId)
        } catch (e: Exception) {
            throw Exception("Failed to fetch recent chats for user: $userId", e)
        }
    }
    override suspend fun updateRecentChats(homeUserId: String, awayUser: User, message: String) {
        try {
            recentChatsDataSource.updateRecentChat(homeUserId, awayUser, message)
        } catch (e: Exception) {
            throw Exception("Failed to update recent chats for user: $homeUserId", e)
        }
    }
}
