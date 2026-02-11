package com.module.core_api_storage.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.module.core_api_storage.database.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DatabaseConstants.TABLE_EPISODE)
data class DramaEpisodeEntity(
    @PrimaryKey
    @ColumnInfo(name = "episodeId")
    val episodeId: String,

    @ColumnInfo(name = "dramaOwnerId")
    val dramaOwnerId: String,

    @ColumnInfo(name = "urlStream")
    val urlStream: String,

    @ColumnInfo(name = "serialNo")
    val serialNo: String
) : Parcelable