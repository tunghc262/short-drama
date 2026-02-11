package com.module.core_api_storage.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.module.core_api_storage.database.DatabaseConstants
import com.module.core_api_storage.database.collection_data.DramaWithGenres
import com.module.core_api_storage.database.entity.DramaEpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommonDao {
    //get drama by category
    @Transaction
    @Query(
        """
    SELECT * FROM ${DatabaseConstants.TABLE_DRAMA} drama
    INNER JOIN ${DatabaseConstants.TABLE_DRAMA_CATEGORY} category
    ON drama.dramaId = category.dramaId
    WHERE category.categoryId = :categoryId
"""
    )
    fun getDramasByCategory(categoryId: String): Flow<List<DramaWithGenres>>

    // get all episode by dramaId
    @Query("SELECT * FROM episode_table WHERE dramaOwnerId = :dramaId ORDER BY serialNo ASC")
    fun getEpisodesOfDrama(dramaId: String): Flow<List<DramaEpisodeEntity>>

    //get all drama with genres
    @Transaction
    @Query("SELECT * FROM ${DatabaseConstants.TABLE_DRAMA}")
    fun getAllDramaWithGenres(): Flow<List<DramaWithGenres>>

    //get all drama by genres
    @Transaction
    @Query(
        """
    SELECT DISTINCT d.* FROM ${DatabaseConstants.TABLE_DRAMA} d
    INNER JOIN ${DatabaseConstants.TABLE_DRAMA_GENRES} dg
    ON d.dramaId = dg.dramaId
    WHERE dg.genresId = :genresId
"""
    )
    suspend fun getDramaWithGenresByGenre(genresId: String): List<DramaWithGenres>
}