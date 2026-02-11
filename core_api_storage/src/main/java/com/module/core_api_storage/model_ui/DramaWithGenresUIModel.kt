package com.module.core_api_storage.model_ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaWithGenresUIModel(
    val dramaUIModel: DramaUIModel,
    val dramaGenresUIModel: List<DramaGenresUIModel>
) : Parcelable