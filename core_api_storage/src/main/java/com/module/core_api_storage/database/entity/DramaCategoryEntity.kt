package com.module.core_api_storage.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.module.core_api_storage.database.DatabaseConstants
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = DatabaseConstants.TABLE_CATEGORY)
data class DramaCategoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "categoryId")
    val categoryId: String,

    @ColumnInfo(name = "categoryName")
    val categoryName: String
) : Parcelable