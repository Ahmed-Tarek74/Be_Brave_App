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
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideLoginUseCase(
        authRepository: AuthRepository,
        deviceTokenRepository: DeviceTokenRepository,
        userRepository: UserRepository
    ): LoginUseCase = LoginUseCase(authRepository, deviceTokenRepository, userRepository)
    @ViewModelScoped
    @Provides
    fun provideRegistrationUseCase(authRepository: AuthRepository, userRepository: UserRepository):
            RegistrationUseCase = RegistrationUseCase(authRepository, userRepository)
    @ViewModelScoped
    @Provides
    fun provideDateFormatterUseCase():
            DateFormatterUseCase = DateFormatterUseCase()
    @ViewModelScoped
    @Provides
    fun provideGetUsersUseCase(userRepository: UserRepository):
            GetUsersUseCase = GetUsersUseCase(userRepository)
    @ViewModelScoped
    @Provides
    fun provideLogOutUseCase(
        authRepository: AuthRepository,
        userRepository: UserRepository
    ):
            LogOutUseCase = LogOutUseCase(authRepository, userRepository)
    @ViewModelScoped
    @Provides
    fun provideGetCachedUserUseCase(userRepository: UserRepository):
            GetCachedUserUseCase = GetCachedUserUseCase(userRepository)
    @ViewModelScoped
    @Provides
    fun provideSendMessageUseCase(
        messagesRepository: MessagesRepository,
        recentChatsRepository: RecentChatsRepository,
    ): SendMessageUseCase =
        SendMessageUseCase(messagesRepository, recentChatsRepository)
    @ViewModelScoped
    @Provides
    fun provideSendNotificationUseCase(
        notificationRepository: NotificationRepository,
        deviceTokenRepository: DeviceTokenRepository
    ): SendNotificationUseCase =
        SendNotificationUseCase(notificationRepository, deviceTokenRepository)
    @ViewModelScoped
    @Provides
    fun provideGetRecentChatsUseCase(
        recentChatsRepository: RecentChatsRepository,
    ):
            GetRecentChatsUseCase = GetRecentChatsUseCase(recentChatsRepository)
    @ViewModelScoped
    @Provides
    fun provideGetRecentMessagesUseCase(messagesRepository: MessagesRepository):
            GetRecentMessagesUseCase = GetRecentMessagesUseCase(messagesRepository)
}

