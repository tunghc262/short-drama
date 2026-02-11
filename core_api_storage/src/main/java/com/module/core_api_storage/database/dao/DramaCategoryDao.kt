package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.entity.DramaCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaCategoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDramaCategory(dramaCategoryEntities: List<DramaCategoryEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_CATEGORY}")
    fun getAllDramaCategory(): Flow<List<DramaCategoryEntity>>
}