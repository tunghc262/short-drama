package com.shortdrama.movie.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core_api.model.core.GenreTVSeriesModel
import com.shortdrama.movie.app.AppConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = AppConstants.TABLE_NAME_HISTORY_WATCH)
data class HistoryWatchEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "originalName")
    var originalName: String?,

    @ColumnInfo(name = "overview")
    var overview: String?,

    @ColumnInfo(name = "numberOfSeasons")
    var numberOfSeasons: Int?,

    @ColumnInfo(name = "numberOfEpisodes")
    var numberOfEpisodes: Int?,

    @ColumnInfo(name = "posterPath")
    var posterPath: String?,

    @ColumnInfo(name = "genres")
    val genres: List<GenreTVSeriesModel>?,

    @ColumnInfo(name = "episodeCurrentId")
    var episodeCurrentId: Int? = 0,

    @ColumnInfo(name = "episodeCurrentNo")
    var episodeCurrentNo: Int? = 0,

    @ColumnInfo(name = "movie_timestamp ")
    var timestamp: Long = 0L,
) : Parcelable
