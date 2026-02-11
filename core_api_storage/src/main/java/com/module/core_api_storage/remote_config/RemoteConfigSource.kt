package com.module.core_api_storage.remote_config

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.module.core_api_storage.common.toEntity
import com.module.core_api_storage.database.DramaRepository
import com.module.core_api_storage.model_dto.DramaCategoryModel
import com.module.core_api_storage.model_dto.DramaEpisodeModel
import com.module.core_api_storage.model_dto.DramaGenresModel
import com.module.core_api_storage.model_dto.DramaModel
import com.module.core_api_storage.model_dto_relational.DramaCategoryCrossModel
import com.module.core_api_storage.model_dto_relational.DramaGenresCrossModel
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class RemoteConfigSource @Inject constructor(private val dramaRepository: DramaRepository) {

    companion object {
        private const val KEY_DRAMA = "drama"
        private const val KEY_EPISODE = "episode"
        private const val KEY_CATEGORY = "category"
        private const val KEY_GENRES = "genres"

        //relationship
        private const val KEY_DRAMA_CATEGORY = "drama_category_cross"
        private const val KEY_DRAMA_GENRES = "drama_genres_cross"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun initRemoteConfigDatabase() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            auth.signInAnonymously()
                .addOnSuccessListener {
                    initRemoteConfig()
                }
                .addOnFailureListener { e ->

                }
        } else {
            initRemoteConfig()
        }
    }

    private fun initRemoteConfig() {
        Log.e("TAG_test_all_data", "initRemoteConfig: 1")
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                Log.e("TAG_test_all_data", "initRemoteConfig: 2")
                if (task.isSuccessful) {
                    Log.e("TAG_test_all_data", "initRemoteConfig: 3")
                    getAllDataRemote(remoteConfig)
                } else {
                    Log.e("TAG_test_all_data", "Fetch Remote Config FAILED", task.exception)
                }
            }
    }

    private fun getAllDataRemote(remoteConfig: FirebaseRemoteConfig) {
        scope.launch {
            try {
                // drama
                remoteConfig.getString(KEY_DRAMA).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaModel>>() {}.type
                    val dramas: List<DramaModel> = Gson().fromJson(json, type)
                    Log.e("TAG_test_all_data", "getAllDataRemote: ${dramas.size}")
                    dramaRepository.addDrama(dramas.map { it.toEntity() })
                }

                // episode
                remoteConfig.getString(KEY_EPISODE).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaEpisodeModel>>() {}.type
                    val episodes: List<DramaEpisodeModel> = Gson().fromJson(json, type)
                    dramaRepository.addEpisode(episodes.map { it.toEntity() })
                }

                // category
                remoteConfig.getString(KEY_CATEGORY).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaCategoryModel>>() {}.type
                    val categories: List<DramaCategoryModel> = Gson().fromJson(json, type)
                    dramaRepository.addCategory(categories.map { it.toEntity() })
                }

                // genres
                remoteConfig.getString(KEY_GENRES).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaGenresModel>>() {}.type
                    val genres: List<DramaGenresModel> = Gson().fromJson(json, type)
                    dramaRepository.addGenres(genres.map { it.toEntity() })
                }

                // drama category cross
                remoteConfig.getString(KEY_DRAMA_CATEGORY).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaCategoryCrossModel>>() {}.type
                    val dramaCategories: List<DramaCategoryCrossModel> = Gson().fromJson(json, type)
                    dramaRepository.addDramaCategoryCross(dramaCategories.map { it.toEntity() })
                }

                // drama genres cross
                remoteConfig.getString(KEY_DRAMA_GENRES).takeIf { it.isNotEmpty() }?.let { json ->
                    val type = object : TypeToken<List<DramaGenresCrossModel>>() {}.type
                    val dramaGenres: List<DramaGenresCrossModel> = Gson().fromJson(json, type)
                    dramaRepository.addDramaGenresCross(dramaGenres.map { it.toEntity() })
                }

            } catch (e: Exception) {
                Log.e("RemoteConfigSource", "Parse or insert failed", e)
            }
        }
    }
}