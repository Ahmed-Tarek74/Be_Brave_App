package com.compose.chatapp.di

import android.content.Context
import android.content.res.AssetManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.compose.data.datasource.auth.AuthenticationDataSourceImpl
import com.compose.data.datasource.user.UserPreferencesManagerImpl
import com.compose.data.constatnts.PreferenceDataStoreConstants
import com.compose.data.datasource.auth.AuthenticationDataSource
import com.compose.data.datasource.deviceToken.DeviceTokenDataSourceImpl
import com.compose.data.datasource.deviceToken.DeviceTokenDataSource
import com.compose.data.datasource.message.MessageDataSource
import com.compose.data.datasource.message.MessageDataSourceImpl
import com.compose.data.datasource.notification.NotificationDataSource
import com.compose.data.datasource.notification.NotificationDataSourceImpl
import com.compose.data.datasource.recentChat.RecentChatDataSourceImpl
import com.compose.data.datasource.recentChat.RecentChatDataSource
import com.compose.data.datasource.user.RemoteUserDataManagerImpl
import com.compose.data.datasource.user.RemoteUserDataManager
import com.compose.data.datasource.user.UserDataSource
import com.compose.data.datasource.user.UserPreferencesManager
import com.compose.data.datasource.user.UserDataSourceImpl
import com.compose.data.remote.FcmApi
import com.compose.data.repo.AuthRepositoryImpl
import com.compose.data.repo.MessagesRepositoryImpl
import com.compose.data.repo.DeviceTokenRepositoryImpl
import com.compose.data.repo.UserRepositoryImpl
import com.compose.data.repo.NotificationRepositoryImpl
import com.compose.data.repo.RecentChatsRepositoryImpl
import com.compose.data.services.ITokenService
import com.compose.data.services.TokenService
import com.compose.data.utils.FcmServiceUtil
import com.compose.data.utils.FirebaseEventLogger
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserRepository
import com.compose.domain.repos.NotificationRepository
import com.compose.domain.repos.RecentChatsRepository
import com.compose.domain.utils.EventLogger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(PreferenceDataStoreConstants.DATASTORE_NAME)

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseCrashlytics(): FirebaseCrashlytics {
        return FirebaseCrashlytics.getInstance()
    }

    @Provides
    @Singleton
    fun provideEventLogger(crashlytics: FirebaseCrashlytics): EventLogger =
        FirebaseEventLogger(crashlytics)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideAssetManager(@ApplicationContext context: Context): AssetManager {
        return context.assets
    }

    @Provides
    @Singleton
    fun provideFcmServiceUtil(assetManager: AssetManager): FcmServiceUtil {
        return FcmServiceUtil(assetManager)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthDataSource(
        firebaseAuth: FirebaseAuth,
        eventLogger: EventLogger
    ): AuthenticationDataSource =
        AuthenticationDataSourceImpl(firebaseAuth, eventLogger)

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: AuthenticationDataSource,
    ): AuthRepository = AuthRepositoryImpl(firebaseAuthDataSource)

    @Provides
    @Singleton
    fun provideRemoteUserDataManager(
        database: FirebaseDatabase
    ): RemoteUserDataManager = RemoteUserDataManagerImpl(database)

    @Provides
    @Singleton
    fun provideUserPreferencesManager(dataStore: DataStore<Preferences>):
            UserPreferencesManager = UserPreferencesManagerImpl(dataStore)

    @Provides
    @Singleton
    fun provideUserDataSource(
        remoteUserDataManager: RemoteUserDataManager,
        userPreferencesManager: UserPreferencesManager
    ): UserDataSource = UserDataSourceImpl(remoteUserDataManager, userPreferencesManager)

    @Provides
    @Singleton
    fun provideUserRepo(userDataSource: UserDataSource):
            UserRepository = UserRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideMessageDataSource(firebaseDatabase: FirebaseDatabase, eventLogger: EventLogger):
            MessageDataSource = MessageDataSourceImpl(firebaseDatabase, eventLogger)

    @Provides
    @Singleton
    fun provideChatRepo(messageDataSource: MessageDataSource):
            MessagesRepository = MessagesRepositoryImpl(messageDataSource)

    @Provides
    @Singleton
    fun provideNotificationDataSource(fcmApi: FcmApi, eventLogger: EventLogger):
            NotificationDataSource = NotificationDataSourceImpl(fcmApi, eventLogger)

    @Provides
    @Singleton
    fun provideNotificationRepo(notificationDataSource: NotificationDataSource):
            NotificationRepository = NotificationRepositoryImpl(notificationDataSource)

    @Provides
    @Singleton
    fun provideTokenService():
            ITokenService = TokenService()

    @Provides
    @Singleton
    fun provideDeviceTokenDataSource(
        database: FirebaseDatabase,
        tokenService: ITokenService,
        eventLogger: EventLogger
    ): DeviceTokenDataSource = DeviceTokenDataSourceImpl(database, tokenService, eventLogger)

    @Provides
    @Singleton
    fun provideDeviceTokenRepo(
        deviceTokenDataSource: DeviceTokenDataSource
    ):
            DeviceTokenRepository = DeviceTokenRepositoryImpl(deviceTokenDataSource)

    @Provides
    @Singleton
    fun provideRecentChatsDataSource(firebaseDatabase: FirebaseDatabase):
            RecentChatDataSource = RecentChatDataSourceImpl(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRecentChatsRepo(recentChatDataSource: RecentChatDataSource):
            RecentChatsRepository = RecentChatsRepositoryImpl(recentChatDataSource)
}