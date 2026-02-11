package com.shortdrama.movie.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.module.core_api_storage.model_ui.DramaGenresUIModel

class AppConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGenresList(list: List<DramaGenresUIModel>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toGenresList(json: String?): List<DramaGenresUIModel>? {
        return json?.let {
            val type = object : TypeToken<List<DramaGenresUIModel>>() {}.type
            gson.fromJson(it, type)
        }
    }
}