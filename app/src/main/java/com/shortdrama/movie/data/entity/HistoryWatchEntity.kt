package com.shortdrama.movie.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shortdrama.movie.app.AppConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = AppConstants.TABLE_NAME_HISTORY_WATCH)
data class HistoryWatchEntity(
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

    @ColumnInfo(name = "episode_id")
    val episodeId: String,

    @ColumnInfo(name = "episode_no")
    val episodeNo: String,

    @ColumnInfo(name = "movie_timestamp")
    var timestamp: Long = 0L,
) : Parcelable
