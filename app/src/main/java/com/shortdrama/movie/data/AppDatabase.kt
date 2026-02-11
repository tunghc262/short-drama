package com.shortdrama.movie.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.dao.HistoryWatchDao
import com.shortdrama.movie.data.dao.MovieFavoriteDao
import com.shortdrama.movie.data.dao.UserDao
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import com.shortdrama.movie.data.entity.UserEntity

@Database(
    entities = [UserEntity::class, MovieFavoriteEntity::class, HistoryWatchEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(AppConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val historyWatchDao: HistoryWatchDao
    abstract val movieFavoriteDao: MovieFavoriteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    AppConstants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}