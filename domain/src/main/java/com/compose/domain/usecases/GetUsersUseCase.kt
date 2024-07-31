package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.GetUsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class GetUsersUseCase(private val getUsersRepository: GetUsersRepository) {

    fun searchUsers(query: String, userId: String): Flow<Result<List<User>>> = flow {
        try {
            val users =
                getUsersRepository.searchUsers(query, userId).first()
            emit(Result.success(users))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}