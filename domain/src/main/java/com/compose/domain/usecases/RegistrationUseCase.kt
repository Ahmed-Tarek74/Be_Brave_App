package com.compose.domain.usecases

import com.compose.domain.entities.User
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.UserRepository

class RegistrationUseCase(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(user: User, confirmationPassword: String): User {
        if (!isPasswordsMatch(user.password, confirmationPassword)) {
            throw (Exception("Passwords do not match"))
        }
        val registeredUser =authRepository.register(user)
        userRepository.addUser(registeredUser)
        return registeredUser
    }

    private fun isPasswordsMatch(password: String, confirmationPassword: String): Boolean {
        return password == confirmationPassword
    }
}