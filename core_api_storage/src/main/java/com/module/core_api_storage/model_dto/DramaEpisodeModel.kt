package com.module.core_api_storage.model_dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DramaEpisodeModel(
    val episodeId: String,
    val dramaOwnerId: String,
    val urlStream: String,
    val serialNo: String
) : Parcelable