package com.module.core_api_storage.database.relational_entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.module.core_api_storage.database.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = DatabaseConstants.TABLE_DRAMA_GENRES,
    primaryKeys = ["dramaId", "genresId"]
)
data class DramaGenresCrossRefEntity(
    @ColumnInfo(name = "dramaId")
    val dramaId: String,

    @ColumnInfo(name = "genresId")
    val genresId: String
) : Parcelable