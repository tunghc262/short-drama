package com.shortdrama.movie.data

import androidx.room.TypeConverter
import com.example.core_api.model.core.GenreTVSeriesModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(value: List<GenreTVSeriesModel>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toGenreList(value: String): List<GenreTVSeriesModel> {
        val type = object : TypeToken<List<GenreTVSeriesModel>>() {}.type
        return gson.fromJson(value, type)
    }
}