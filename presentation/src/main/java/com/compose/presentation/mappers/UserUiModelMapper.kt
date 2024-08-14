package com.compose.presentation.mappers

import com.compose.domain.entities.User
import com.compose.presentation.R
import com.compose.presentation.models.UserUiModel

fun User.mapToUserUiModel(): UserUiModel {
    return UserUiModel(
        userId = this.userId,
        username = this.username,
        profilePicture = this.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture }
    )
}

fun UserUiModel.mapToUser(): User {
    val profilePictureUrl =
        if (this.profilePicture == R.drawable.default_profile_picture) {
            ""
        } else {
            this.profilePicture as String
        }
    return User(
        userId = this.userId,
        username = this.username,
        profilePictureUrl = profilePictureUrl
    )

}