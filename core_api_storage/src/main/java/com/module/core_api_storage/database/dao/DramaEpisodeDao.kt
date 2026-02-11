package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.entity.DramaEpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaEpisodeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDramaEpisode(dramaEpisodeEntities: List<DramaEpisodeEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_EPISODE}")
    fun getAllDramaEpisode(): Flow<List<DramaEpisodeEntity>>
}