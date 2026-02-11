package com.module.core_api_storage.model_ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaUIModel(
    val dramaId: String,
    val dramaName: String,
    val dramaDescription: String,
    val dramaThumb: String,
    val dramaTrailer: String,
    val totalEpisode: String
) : Parcelable