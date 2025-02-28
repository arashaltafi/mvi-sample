package com.arash.altafi.mvisample.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.arash.altafi.mvisample.BuildConfig
import com.arash.altafi.mvisample.data.api.CelebrityService
import com.arash.altafi.mvisample.data.api.PagingService
import com.arash.altafi.mvisample.data.api.UserService
import com.arash.altafi.mvisample.data.dataSource.TestDataSource
import com.arash.altafi.mvisample.data.db.TestDetailDao
import com.arash.altafi.mvisample.data.repository.CelebrityRepository
import com.arash.altafi.mvisample.data.repository.UserRepository
import com.arash.altafi.mvisample.data.repository.DataStoreRepository
import com.arash.altafi.mvisample.data.repository.PagingRepository
import com.arash.altafi.mvisample.data.repository.TestRepository
import com.arash.altafi.mvisample.utils.EncryptionUtils
import com.arash.altafi.mvisample.utils.JsonUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideDataStoreRepository(
        dataStore: DataStore<Preferences>,
        encryptionUtils: EncryptionUtils,
        jsonUtils: JsonUtils
    ) = DataStoreRepository(dataStore, encryptionUtils, jsonUtils)

    @Singleton
    @Provides
    fun provideUserRepository(
        userService: UserService,
    ) = UserRepository(userService)

    @Singleton
    @Provides
    fun providePagingRepository(
        userService: PagingService,
    ) = PagingRepository(userService)

    @Singleton
    @Provides
    fun provideTestRepository(
        testDataSource: TestDataSource,
        userDao: TestDetailDao,
    ) = TestRepository(testDataSource, userDao)

    @Singleton
    @Provides
    fun provideCelebrityRepository(
        celebrityService: CelebrityService,
    ) = CelebrityRepository(celebrityService)

    @Provides
    @Singleton
    fun provideServerUrl(): String {
        return BuildConfig.BASE_URL
    }
}