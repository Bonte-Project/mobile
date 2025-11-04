package ua.nure.bonte.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.auth.AuthRepositoryImpl
import ua.nure.bonte.repository.token.TokenRepository
import ua.nure.bonte.repository.token.TokenRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTokenRepository(
        dataStore: DataStore<Preferences>
    ): TokenRepository = TokenRepositoryImpl(
        dataStore = dataStore
    )

    @Provides
    fun provideAuthRepository(
        httpClient: HttpClient
    ): AuthRepository = AuthRepositoryImpl(
        httpClient = httpClient
    )
}