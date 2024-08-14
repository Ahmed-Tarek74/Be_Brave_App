package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
class RegistrationUseCase(private val authRepository: AuthRepository) {

    suspend operator fun invoke(user: User, confirmationPassword: String): User{
        if (!isPasswordsMatch(user.password, confirmationPassword)) {
          throw (Exception("Passwords do not match"))
        }
        return authRepository.register(user)
    }
    private fun isPasswordsMatch(password: String, confirmationPassword: String): Boolean {
        return password == confirmationPassword
    }
}