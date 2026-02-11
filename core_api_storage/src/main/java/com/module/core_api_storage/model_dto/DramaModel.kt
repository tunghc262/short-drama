package com.module.core_api_storage.model_dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaModel(
    val dramaId: String,
    val dramaName: String,
    val dramaDescription: String,
    val dramaThumb: String,
    val dramaTrailer: String,
    val totalEpisode: String
) : Parcelable