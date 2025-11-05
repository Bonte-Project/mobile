package ua.nure.bonte.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CloseableCoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.auth.AuthRepositoryImpl
import ua.nure.bonte.repository.db.DbRepository
import ua.nure.bonte.repository.db.DbRepositoryImpl
import ua.nure.bonte.repository.token.ProfileRepository
import ua.nure.bonte.repository.token.ProfileRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProfileRepository(
        dataStore: DataStore<Preferences>
    ): ProfileRepository = ProfileRepositoryImpl(
        dataStore = dataStore
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideAuthRepository(
        httpClient: HttpClient,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
        dbRepository: DbRepository,
        profileRepository: ProfileRepository,
    ): AuthRepository = AuthRepositoryImpl(
        httpClient = httpClient,
        dbDeliveryDispatcher = dbDeliveryDispatcher,
        dbRepository = dbRepository,
        profileRepository = profileRepository
    )

    @Provides
    @Singleton
    fun provideDbRepository(
        @ApplicationContext context: Context,
        profileRepository: ProfileRepository
    ): DbRepository = DbRepositoryImpl(
        context = context,
        profileRepository = profileRepository
    )
}