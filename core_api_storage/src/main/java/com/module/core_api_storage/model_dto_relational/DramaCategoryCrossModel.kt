package com.module.core_api_storage.model_dto_relational

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaCategoryCrossModel(
    val dramaId: String,
    val categoryId: String
) : Parcelable