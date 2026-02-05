package com.example.core_api.di

import android.content.Context
import com.example.core_api.api.MoviesApiService
import com.example.core_api.api.TokenInterceptor
import com.example.core_api.auth.AuthenApi
import com.example.core_api.auth.TokenAuthenticator
import com.example.core_api.auth.TokenRefresher
import com.example.core_api.auth.TokenStore
import com.example.core_api.utils.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideTokenStore(
        @ApplicationContext context: Context
    ) = TokenStore(context)

    @Provides
    @Singleton
    fun provideAuthApi(): AuthenApi =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenApi::class.java)

    @Provides
    @Singleton
    fun provideTokenRefresher(
        api: AuthenApi,
        store: TokenStore
    ) = TokenRefresher(api, store)

    @Provides
    @Singleton
    fun provideOkHttp(
        store: TokenStore,
        refresher: TokenRefresher
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                TokenInterceptor(store, refresher)
            )
            .authenticator(
                TokenAuthenticator(refresher)
            )
            .build()

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    fun provideApiService(retrofit: Retrofit): MoviesApiService {
        return retrofit.create(MoviesApiService::class.java)
    }
}