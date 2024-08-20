package com.compose.presentation.viewStates

import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.compose.presentation.R

data class RegistrationViewState(
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val confirmationPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val isConfirmationPasswordVisible: Boolean = false,
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val passwordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val passwordTrailingIcon: Int = R.drawable.baseline_visibility_off_24,
    val passwordIconDescription: Int= R.string.show_password,
    val confirmationPasswordVisualTransformation: VisualTransformation = PasswordVisualTransformation(),
    val confirmationPasswordTrailingIcon: Int = R.drawable.baseline_visibility_off_24,
    val confirmationPasswordIconDescription: Int= R.string.show_password

)
