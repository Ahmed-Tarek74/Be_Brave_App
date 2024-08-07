package com.compose.presentation.mappers

import com.compose.domain.entities.User
import com.compose.presentation.R
import com.compose.presentation.models.UserUiModel
import javax.inject.Inject

class UserUiModelMapper @Inject constructor() {
    fun mapToUserUiModel(user: User): UserUiModel {
        return UserUiModel(
            userId = user.userId,
            username = user.username,
            profilePicture = user.profilePictureUrl.ifEmpty { R.drawable.default_profile_picture }
        )
    }
    fun mapToUserDomainModel(userUiModel: UserUiModel): User {
        val profilePictureUrl =
            if (userUiModel.profilePicture == R.drawable.default_profile_picture) {
                ""
            } else {
                userUiModel.profilePicture as String
            }
        return User(
            userId = userUiModel.userId,
            username = userUiModel.username,
            profilePictureUrl = profilePictureUrl
        )
    }
}