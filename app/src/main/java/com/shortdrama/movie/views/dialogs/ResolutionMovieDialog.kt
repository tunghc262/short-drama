package com.shortdrama.movie.views.dialogs

import android.app.Activity
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.PlayMovieSpeedModel
import com.shortdrama.movie.databinding.ItemPlaySpeedMovieBinding
import com.shortdrama.movie.views.bases.BaseBottomSheetDialogFragment

class ResolutionMovieDialog(
    val activity: Activity,
    val currentSpeed: Int,
    val onClickItem: (PlayMovieSpeedModel)
) : BaseBottomSheetDialogFragment<ItemPlaySpeedMovieBinding>() {
    override fun getLayoutFragment(): Int = R.layout.item_play_speed_movie
    override fun initViews() {
        super.initViews()
    }

    override fun onClickViews() {
        super.onClickViews()
    }

}