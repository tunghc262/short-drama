package com.shortdrama.movie.app

import android.app.Application
import android.content.Context
import com.shortdrama.movie.data.AppDatabase
import com.shortdrama.movie.data.dao.HistoryWatchDao
import com.shortdrama.movie.data.dao.MovieFavoriteDao
import com.shortdrama.movie.data.dao.UserDao
import com.shortdrama.movie.network.ApiServer
import com.shortdrama.movie.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providerApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase = AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao
    }

    @Provides
    fun provideHistoryWatchDao(appDatabase: AppDatabase): HistoryWatchDao {
        return appDatabase.historyWatchDao
    }

    @Provides
    fun provideMovieFavorite(appDatabase: AppDatabase): MovieFavoriteDao {
        return appDatabase.movieFavoriteDao
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(context: Context, baseUrl: String): Retrofit =
        RetrofitClient.getClient(context, baseUrl)

    @Provides
    @Singleton
    fun providerApiService(retrofit: Retrofit): ApiServer = retrofit.create(ApiServer::class.java)
}
