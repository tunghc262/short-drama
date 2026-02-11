package com.shortdrama.movie.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shortdrama.movie.app.AppConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = AppConstants.TABLE_NAME_MOVIE_FAVORITE)
data class MovieFavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = "drama_id")
    val dramaId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "thumb")
    val thumb: String,

    @ColumnInfo(name = "total_episode")
    val totalEpisode: String,

    @ColumnInfo(name = "drama_trailer")
    val dramaTrailer: String,

    @ColumnInfo(name = "genres_json")
    val genresJson: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()
) : Parcelable