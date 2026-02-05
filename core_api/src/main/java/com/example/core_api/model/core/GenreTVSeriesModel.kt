package com.example.core_api.model.core

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GenreTVSeriesModel(
    val id: Int,
    val name: String
) : Parcelable