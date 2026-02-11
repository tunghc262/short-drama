package com.module.core_api_storage.model_dto_relational

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaGenresCrossModel(
    val dramaId: String,
    val genresId: String
) : Parcelable
