package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class RegistrationUseCase(private val authRepository: AuthRepository) {

    operator fun invoke(user: User, confirmationPassword: String): Flow<Result<User>> {
        if (!isPasswordsMatch(user.password, confirmationPassword)) {
            return flow { emit(Result.failure(Exception("Passwords do not match"))) }
        }
        return authRepository.register(user)
    }

    private fun isPasswordsMatch(password: String, confirmationPassword: String): Boolean {
        return password == confirmationPassword
    }
}