package com.shortdrama.movie.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OnboardModel(
    var image: Int = 0,
    var title: String = "",
    var content: String = ""
) : Parcelable