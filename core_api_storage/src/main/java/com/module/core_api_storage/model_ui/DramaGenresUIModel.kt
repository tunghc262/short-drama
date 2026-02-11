package com.module.core_api_storage.model_ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaGenresUIModel(
    val genresId: String,
    val genresName: String
) : Parcelable