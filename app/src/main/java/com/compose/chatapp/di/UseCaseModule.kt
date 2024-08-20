package com.compose.chatapp.di

import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserRepository
import com.compose.domain.repos.NotificationRepository
import com.compose.domain.repos.RecentChatsRepository
import com.compose.domain.usecases.DateFormatterUseCase
import com.compose.domain.usecases.SendMessageUseCase
import com.compose.domain.usecases.GetCachedUserUseCase
import com.compose.domain.usecases.GetRecentChatsUseCase
import com.compose.domain.usecases.GetRecentMessagesUseCase
import com.compose.domain.usecases.GetUsersUseCase
import com.compose.domain.usecases.LogOutUseCase
import com.compose.domain.usecases.LoginUseCase
import com.compose.domain.usecases.RegistrationUseCase
import com.compose.domain.usecases.SendNotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        deviceTokenRepository: DeviceTokenRepository,
        userRepository: UserRepository
    ): LoginUseCase = LoginUseCase(authRepository, deviceTokenRepository, userRepository)

    @Provides
    fun provideRegistrationUseCase(authRepository: AuthRepository,userRepository: UserRepository):
            RegistrationUseCase = RegistrationUseCase(authRepository,userRepository)

    @Provides
    fun provideDateFormatterUseCase():
            DateFormatterUseCase = DateFormatterUseCase()
    @Provides
    fun provideGetUsersUseCase(userRepository: UserRepository):
            GetUsersUseCase = GetUsersUseCase(userRepository)

    @Provides
    fun provideLogOutUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ):
            LogOutUseCase = LogOutUseCase(authRepository, userRepository)

    @Provides
    fun provideGetCachedUserUseCase(userRepository: UserRepository):
            GetCachedUserUseCase = GetCachedUserUseCase(userRepository)

    @Provides
    fun provideSendMessageUseCase(
        messagesRepository: MessagesRepository,
        recentChatsRepository: RecentChatsRepository,
    ): SendMessageUseCase =
        SendMessageUseCase(messagesRepository, recentChatsRepository)

    @Provides
    fun provideSendNotificationUseCase(
        notificationRepository: NotificationRepository,
        deviceTokenRepository: DeviceTokenRepository
    ): SendNotificationUseCase =
        SendNotificationUseCase(notificationRepository, deviceTokenRepository)

    @Provides
    fun provideGetRecentChatsUseCase(
        recentChatsRepository: RecentChatsRepository,
    ):
            GetRecentChatsUseCase = GetRecentChatsUseCase(recentChatsRepository)

    @Provides
    fun provideGetRecentMessagesUseCase(messagesRepository: MessagesRepository):
            GetRecentMessagesUseCase = GetRecentMessagesUseCase(messagesRepository)
}

