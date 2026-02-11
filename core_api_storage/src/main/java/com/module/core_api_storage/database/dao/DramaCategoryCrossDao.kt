package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.relational_entity.DramaCategoryCrossRefEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DramaCategoryCrossDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDramaCategoryCross(dramaCategoryCrossRefEntities: List<DramaCategoryCrossRefEntity>)

    @Query("SELECT * FROM ${DatabaseConstants.TABLE_DRAMA_CATEGORY}")
    fun getAllDramaCategoryCross(): Flow<List<DramaCategoryCrossRefEntity>>
}