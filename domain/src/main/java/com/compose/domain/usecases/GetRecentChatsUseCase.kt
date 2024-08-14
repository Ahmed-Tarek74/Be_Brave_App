package com.compose.domain.usecases

import com.compose.domain.entities.RecentChat
import com.compose.domain.repos.RecentChatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GetRecentChatsUseCase(
    private val recentChatsRepository: RecentChatsRepository
) {
    suspend operator fun invoke(userId: String): Flow<List<RecentChat>> =
        withContext(Dispatchers.IO) {
            recentChatsRepository.getRecentChats(userId)
        }
}
