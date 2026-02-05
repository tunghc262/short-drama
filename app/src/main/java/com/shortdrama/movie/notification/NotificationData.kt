package com.shortdrama.movie.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationData(val title: String, val message: String, val res: Int) : Parcelable
