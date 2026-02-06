package com.shortdrama.movie.views.dialogs

import android.app.Activity
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.PlayMovieSpeedModel
import com.shortdrama.movie.databinding.DialogPlaySpeedMovieBinding
import com.shortdrama.movie.views.bases.BaseBottomSheetDialogFragment

class PlaySpeedMovieDialog(
    val activity: Activity,
    val currentSpeed: Int,
    val onClickItem: (PlayMovieSpeedModel)
) : BaseBottomSheetDialogFragment<DialogPlaySpeedMovieBinding>() {
    override fun getLayoutFragment(): Int = R.layout.dialog_play_speed_movie
    override fun initViews() {
        super.initViews()
    }

    override fun onClickViews() {
        super.onClickViews()
    }

}