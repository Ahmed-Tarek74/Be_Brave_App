package com.compose.chatapp.di

import android.content.Context
import android.content.res.AssetManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.compose.data.datasource.auth.FirebaseAuthentication
import com.compose.data.datasource.user.UserPreferencesManagerImpl
import com.compose.data.constatnts.PreferenceDataStoreConstants
import com.compose.data.datasource.auth.IAuthentication
import com.compose.data.datasource.deviceToken.DeviceTokenDataSourceImpl
import com.compose.data.datasource.deviceToken.IDeviceTokenDataSource
import com.compose.data.datasource.message.IMessageDataSource
import com.compose.data.datasource.message.FirebaseMessageDataSource
import com.compose.data.datasource.notification.INotificationDataSource
import com.compose.data.datasource.notification.NotificationDataSource
import com.compose.data.datasource.recentChat.FirebaseRecentChatDataSource
import com.compose.data.datasource.recentChat.IRecentChatDataSource
import com.compose.data.datasource.user.RemoteUserDataManagerImpl
import com.compose.data.datasource.user.IRemoteUserDataManager
import com.compose.data.datasource.user.IUserDataSource
import com.compose.data.datasource.user.IUserPreferencesManager
import com.compose.data.datasource.user.UserDataSource
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
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.UserRepository
import com.compose.domain.repos.NotificationRepository
import com.compose.domain.repos.RecentChatsRepository
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirebaseAuthDataSource(firebaseAuth: FirebaseAuth): IAuthentication =
        FirebaseAuthentication(firebaseAuth)

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: IAuthentication,
    ): AuthRepository = AuthRepositoryImpl(firebaseAuthDataSource)

    @Provides
    @Singleton
    fun provideRemoteUserDataManager(
        database: FirebaseDatabase
    ): IRemoteUserDataManager = RemoteUserDataManagerImpl(database)

    @Provides
    @Singleton
    fun provideUserPreferencesManager(dataStore: DataStore<Preferences>):
            IUserPreferencesManager = UserPreferencesManagerImpl(dataStore)

    @Provides
    @Singleton
    fun provideUserDataSource(
        remoteUserDataManager: IRemoteUserDataManager,
        userPreferencesManager: IUserPreferencesManager
    ): IUserDataSource = UserDataSource(remoteUserDataManager, userPreferencesManager)

    @Provides
    @Singleton
    fun provideUserRepo(userDataSource: IUserDataSource):
            UserRepository = UserRepositoryImpl(userDataSource)

    @Provides
    @Singleton
    fun provideMessageDataSource(firebaseDatabase: FirebaseDatabase):
            IMessageDataSource = FirebaseMessageDataSource(firebaseDatabase)

    @Provides
    @Singleton
    fun provideChatRepo(messageDataSource: IMessageDataSource):
            MessagesRepository = MessagesRepositoryImpl(messageDataSource)

    @Provides
    @Singleton
    fun provideNotificationDataSource(fcmApi: FcmApi):
            INotificationDataSource = NotificationDataSource(fcmApi)

    @Provides
    @Singleton
    fun provideNotificationRepo(notificationDataSource: INotificationDataSource):
            NotificationRepository = NotificationRepositoryImpl(notificationDataSource)

    @Provides
    @Singleton
    fun provideTokenService():
            ITokenService = TokenService()

    @Provides
    @Singleton
    fun provideDeviceTokenDataSource(database: FirebaseDatabase, tokenService: ITokenService):
            IDeviceTokenDataSource = DeviceTokenDataSourceImpl(database, tokenService)

    @Provides
    @Singleton
    fun provideDeviceTokenRepo(
        deviceTokenDataSource: IDeviceTokenDataSource
    ):
            DeviceTokenRepository = DeviceTokenRepositoryImpl(deviceTokenDataSource)

    @Provides
    @Singleton
    fun provideRecentChatsDataSource(firebaseDatabase: FirebaseDatabase):
            IRecentChatDataSource = FirebaseRecentChatDataSource(firebaseDatabase)

    @Provides
    @Singleton
    fun provideRecentChatsRepo(recentChatDataSource: IRecentChatDataSource):
            RecentChatsRepository = RecentChatsRepositoryImpl(recentChatDataSource)
}