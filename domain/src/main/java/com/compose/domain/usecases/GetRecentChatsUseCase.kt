package com.compose.domain.usecases

import com.compose.domain.entities.RecentChat
import com.compose.domain.repos.RecentChatsRepository
import kotlinx.coroutines.flow.Flow
class GetRecentChatsUseCase(
    private val recentChatsRepository: RecentChatsRepository
) {
    operator fun invoke(userId: String): Flow<List<RecentChat>> =
        recentChatsRepository.getRecentChats(userId)
}
