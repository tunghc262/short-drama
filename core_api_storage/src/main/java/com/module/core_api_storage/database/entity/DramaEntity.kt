package com.module.core_api_storage.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.module.core_api_storage.database.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DatabaseConstants.TABLE_DRAMA)
data class DramaEntity(
    @PrimaryKey
    @ColumnInfo(name = "dramaId")
    val dramaId: String,

    @ColumnInfo(name = "dramaName")
    val dramaName: String,

    @ColumnInfo(name = "dramaDescription")
    val dramaDescription: String,

    @ColumnInfo(name = "dramaThumb")
    val dramaThumb: String,

    @ColumnInfo(name = "dramaTrailer")
    val dramaTrailer: String,

    @ColumnInfo(name = "totalEpisode")
    val totalEpisode: String,
) : Parcelable