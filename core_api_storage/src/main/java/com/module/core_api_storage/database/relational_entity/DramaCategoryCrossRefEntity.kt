package com.module.core_api_storage.database.relational_entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.module.core_api_storage.database.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = DatabaseConstants.TABLE_DRAMA_CATEGORY,
    primaryKeys = ["dramaId", "categoryId"]
)
data class DramaCategoryCrossRefEntity(
    @ColumnInfo(name = "dramaId")
    val dramaId: String,

    @ColumnInfo(name = "categoryId")
    val categoryId: String
) : Parcelable