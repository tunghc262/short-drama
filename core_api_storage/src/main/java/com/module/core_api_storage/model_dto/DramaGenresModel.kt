package com.module.core_api_storage.model_dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaGenresModel(
    val genresId: String,
    val genresName: String
) : Parcelable