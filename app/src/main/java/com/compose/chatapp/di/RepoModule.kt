package com.compose.chatapp.di

import android.content.Context
import android.content.res.AssetManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.compose.data.datasource.auth.FirebaseAuthentication
import com.compose.data.datasource.user.UserPreferencesDataSource
import com.compose.data.constatnts.PreferenceDataStoreConstants
import com.compose.data.datasource.deviceToken.IDeviceTokenDataSource
import com.compose.data.datasource.deviceToken.FirebaseIDeviceTokenDataSource
import com.compose.data.datasource.message.IMessageDataSource
import com.compose.data.datasource.message.FirebaseMessageDataSource
import com.compose.data.datasource.recentChat.FirebaseRecentChatDataSource
import com.compose.data.datasource.recentChat.IRecentChatDataSource
import com.compose.data.datasource.user.FirebaseUserDataSource
import com.compose.data.datasource.user.IFirebaseUserDataSource
import com.compose.data.datasource.user.IUserPreferencesDataSource
import com.compose.data.remote.FcmApi
import com.compose.data.repo.AuthRepositoryImpl
import com.compose.data.repo.MessagesRepositoryImpl
import com.compose.data.repo.DeviceTokenRepositoryImpl
import com.compose.data.repo.GetUsersRepositoryImpl
import com.compose.data.repo.NotificationRepositoryImpl
import com.compose.data.repo.RecentChatsRepositoryImpl
import com.compose.data.repo.UserPreferencesRepositoryImpl
import com.compose.data.services.ITokenService
import com.compose.data.services.TokenService
import com.compose.data.utils.FcmServiceUtil
import com.compose.domain.repos.AuthRepository
import com.compose.domain.repos.MessagesRepository
import com.compose.domain.repos.DeviceTokenRepository
import com.compose.domain.repos.GetUsersRepository
import com.compose.domain.repos.NotificationRepository
import com.compose.domain.repos.RecentChatsRepository
import com.compose.domain.repos.UserPreferencesRepository
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
    fun provideUserPreferencesDataSource(dataStore: DataStore<Preferences>): IUserPreferencesDataSource {
        return UserPreferencesDataSource(dataStore)
    }
    @Provides
    @Singleton
    fun provideFirebaseAuthProvider(firebaseAuth: FirebaseAuth): FirebaseAuthentication {
        return FirebaseAuthentication(firebaseAuth)
    }
    @Provides
    @Singleton
    fun provideFirebaseUserDataStore(firebaseDatabase: FirebaseDatabase): FirebaseUserDataSource {
        return FirebaseUserDataSource(firebaseDatabase)
    }
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuthDataSource: FirebaseAuthentication,
        firebaseUserDataSource: FirebaseUserDataSource
    ):AuthRepository = AuthRepositoryImpl(firebaseAuthDataSource, firebaseUserDataSource)

    @Provides
    @Singleton
    fun provideListUsersRepo(firebaseUserDataSource: IFirebaseUserDataSource):
            GetUsersRepository = GetUsersRepositoryImpl(firebaseUserDataSource)
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
    fun provideNotificationRepo(fcmApi: FcmApi):
            NotificationRepository = NotificationRepositoryImpl(fcmApi)

    @Provides
    @Singleton
    fun provideTokenService():
            ITokenService = TokenService()
    @Provides
    @Singleton
    fun provideDeviceTokenDataStore(database: FirebaseDatabase):
            IDeviceTokenDataSource = FirebaseIDeviceTokenDataSource(database)

    @Provides
    @Singleton
    fun provideDeviceTokenRepo(deviceTokenDataSource: IDeviceTokenDataSource, tokenService: ITokenService):
            DeviceTokenRepository = DeviceTokenRepositoryImpl(tokenService, deviceTokenDataSource)

    @Provides
    @Singleton
    fun provideUserPreferencesRepo(userPreferencesDataSource: IUserPreferencesDataSource):
            UserPreferencesRepository = UserPreferencesRepositoryImpl(userPreferencesDataSource)
    @Provides
    @Singleton
    fun provideRecentChatsDataSource(firebaseDatabase: FirebaseDatabase):
            IRecentChatDataSource = FirebaseRecentChatDataSource(firebaseDatabase)
    @Provides
    @Singleton
    fun provideRecentChatsRepo(recentChatDataSource: IRecentChatDataSource):
            RecentChatsRepository = RecentChatsRepositoryImpl(recentChatDataSource)
    @Provides
    @Singleton
    fun provideUserDataSource(firebaseDatabase: FirebaseDatabase):
            IFirebaseUserDataSource = FirebaseUserDataSource(firebaseDatabase)

}