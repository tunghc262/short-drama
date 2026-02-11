package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.entity.DramaGenresEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaGenresDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDramaGenres(dramaGenresEntities: List<DramaGenresEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_GENRES}")
    fun getAllDramaGenres(): Flow<List<DramaGenresEntity>>
}