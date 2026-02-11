package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.relational_entity.DramaGenresCrossRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaGenresCrossDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDramaGenresCross(dramaGenresCrossRefEntities: List<DramaGenresCrossRefEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_DRAMA_GENRES}")
    fun getAllDramaGenresCross(): Flow<List<DramaGenresCrossRefEntity>>
}