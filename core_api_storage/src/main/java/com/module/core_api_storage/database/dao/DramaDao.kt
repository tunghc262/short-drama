package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.entity.DramaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDrama(dramas: List<DramaEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_DRAMA}")
    fun getAllDramas(): Flow<List<DramaEntity>>
}