package com.compose.chatapp.di

import android.content.Context
import android.content.res.AssetManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.compose.data.local.DataStoreManager
import com.compose.data.local.PreferenceDataStoreConstants
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
    fun provideDataStoreManager(dataStore: DataStore<Preferences>): DataStoreManager {
        return DataStoreManager(dataStore)
    }
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseDatabase: FirebaseDatabase,
    ):
            AuthRepository = AuthRepositoryImpl(firebaseAuth, firebaseDatabase)

    @Provides
    @Singleton
    fun provideListUsersRepo(firebaseDatabase: FirebaseDatabase):
            GetUsersRepository = GetUsersRepositoryImpl(firebaseDatabase)

    @Provides
    @Singleton
    fun provideChatRepo(firebaseDatabase: FirebaseDatabase):
            MessagesRepository = MessagesRepositoryImpl(firebaseDatabase)

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
    fun provideDeviceTokenRepo(database: FirebaseDatabase, tokenService: ITokenService):
            DeviceTokenRepository = DeviceTokenRepositoryImpl(tokenService, database)

    @Provides
    @Singleton
    fun provideUserPreferencesRepo(dataStoreManager: DataStoreManager):
            UserPreferencesRepository = UserPreferencesRepositoryImpl(dataStoreManager)
    @Provides
    @Singleton
    fun provideRecentChatsRepo(firebaseDatabase: FirebaseDatabase):
            RecentChatsRepository = RecentChatsRepositoryImpl(firebaseDatabase)

}