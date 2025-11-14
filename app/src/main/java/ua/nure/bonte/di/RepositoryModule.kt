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
import ua.nure.bonte.repository.token.TokenRepository
import ua.nure.bonte.repository.token.TokenRepositoryImpl
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.user.UserRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProfileRepository(
        dataStore: DataStore<Preferences>
    ): TokenRepository = TokenRepositoryImpl(
        dataStore = dataStore
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideAuthRepository(
        httpClient: HttpClient,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
        dbRepository: DbRepository,
        tokenRepository: TokenRepository,
    ): AuthRepository = AuthRepositoryImpl(
        httpClient = httpClient,
        dbDeliveryDispatcher = dbDeliveryDispatcher,
        dbRepository = dbRepository,
        tokenRepository = tokenRepository
    )

    @Provides
    @Singleton
    fun provideDbRepository(
        @ApplicationContext context: Context,
        tokenRepository: TokenRepository
    ): DbRepository = DbRepositoryImpl(
        context = context,
        tokenRepository = tokenRepository
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideUserRepository(
        httpClient: HttpClient,
        @DbDeliveryDispatcher dbDeliveryDispatcher: CloseableCoroutineDispatcher,
        dbRepository: DbRepository,
    ): UserRepository = UserRepositoryImpl(
        httpClient = httpClient,
        dbRepository = dbRepository,
        dbDeliveryDispatcher = dbDeliveryDispatcher
    )
}