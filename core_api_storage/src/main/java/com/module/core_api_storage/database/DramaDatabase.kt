package com.module.core_api_storage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.module.core_api_storage.database.dao.CommonDao
import com.module.core_api_storage.database.dao.DramaCategoryCrossDao
import com.module.core_api_storage.database.dao.DramaCategoryDao
import com.module.core_api_storage.database.dao.DramaDao
import com.module.core_api_storage.database.dao.DramaEpisodeDao
import com.module.core_api_storage.database.dao.DramaGenresCrossDao
import com.module.core_api_storage.database.dao.DramaGenresDao
import com.module.core_api_storage.database.entity.DramaCategoryEntity
import com.module.core_api_storage.database.entity.DramaEntity
import com.module.core_api_storage.database.entity.DramaEpisodeEntity
import com.module.core_api_storage.database.entity.DramaGenresEntity
import com.module.core_api_storage.database.relational_entity.DramaCategoryCrossRefEntity
import com.module.core_api_storage.database.relational_entity.DramaGenresCrossRefEntity

@Database(
    entities = [
        DramaEntity::class,
        DramaEpisodeEntity::class,
        DramaCategoryEntity::class,
        DramaGenresEntity::class,
        DramaGenresCrossRefEntity::class,
        DramaCategoryCrossRefEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class DramaDatabase : RoomDatabase() {
    abstract val dramaDao: DramaDao
    abstract val dramaCategoryDao: DramaCategoryDao
    abstract val dramaEpisodeDao: DramaEpisodeDao
    abstract val dramaGenresDao: DramaGenresDao
    abstract val dramaCategoryCrossDao: DramaCategoryCrossDao
    abstract val dramaGenresCrossDao: DramaGenresCrossDao
    abstract val commonDao: CommonDao

    companion object {
        @Volatile
        private var INSTANCE: DramaDatabase? = null

        fun getDatabase(context: Context): DramaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DramaDatabase::class.java,
                    DatabaseConstants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}