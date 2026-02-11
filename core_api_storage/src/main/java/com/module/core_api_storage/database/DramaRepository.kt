package com.module.core_api_storage.database

import com.module.core_api_storage.database.collection_data.DramaWithGenres
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
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DramaRepository @Inject constructor(
    private val dramaDao: DramaDao,
    private val dramaCategoryDao: DramaCategoryDao,
    private val dramaEpisodeDao: DramaEpisodeDao,
    private val dramaGenresDao: DramaGenresDao,
    private val dramaCategoryCrossDao: DramaCategoryCrossDao,
    private val dramaGenresCrossDao: DramaGenresCrossDao,
    private val commonDao: CommonDao
) {
    //insert drama
    suspend fun addDrama(dramas: List<DramaEntity>) {
        dramaDao.insertDrama(dramas)
    }

    //get all drama
    fun getAllDrama(): Flow<List<DramaEntity>> {
        return dramaDao.getAllDramas()
    }

    //get all drama with category
    fun getAllDramaByCategoryWithGenres(categoryId: String): Flow<List<DramaWithGenres>> {
        return commonDao.getDramasByCategory(categoryId)
    }

    //get all drama with genres
    fun getAllDramaWithGenres(): Flow<List<DramaWithGenres>> {
        return commonDao.getAllDramaWithGenres()
    }

    //insert episode
    suspend fun addEpisode(dramaEpisodeEntities: List<DramaEpisodeEntity>) {
        dramaEpisodeDao.insertDramaEpisode(dramaEpisodeEntities)
    }

    //get episode by drama id
    fun getAllEpisodeByDrama(dramaId: String): Flow<List<DramaEpisodeEntity>> {
        return commonDao.getEpisodesOfDrama(dramaId)
    }

    //insert category
    suspend fun addCategory(dramaCategoryEntities: List<DramaCategoryEntity>) {
        dramaCategoryDao.insertDramaCategory(dramaCategoryEntities)
    }

    //insert drama category cross
    suspend fun addDramaCategoryCross(dramaCategoryCrossRefEntities: List<DramaCategoryCrossRefEntity>) {
        dramaCategoryCrossDao.insertDramaCategoryCross(dramaCategoryCrossRefEntities)
    }

    //insert genres
    suspend fun addGenres(dramaGenresEntities: List<DramaGenresEntity>) {
        dramaGenresDao.insertDramaGenres(dramaGenresEntities)
    }

    //get all genres
    fun getAllGenres(): Flow<List<DramaGenresEntity>> {
        return dramaGenresDao.getAllDramaGenres()
    }

    //insert genres cross
    suspend fun addDramaGenresCross(dramaGenresCrossRefEntities: List<DramaGenresCrossRefEntity>) {
        dramaGenresCrossDao.insertDramaGenresCross(dramaGenresCrossRefEntities)
    }

}