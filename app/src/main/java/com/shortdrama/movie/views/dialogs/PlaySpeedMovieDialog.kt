package com.shortdrama.movie.views.dialogs

import android.app.Activity
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.PlayMovieSpeedModel
import com.shortdrama.movie.databinding.DialogPlaySpeedMovieBinding
import com.shortdrama.movie.utils.DataUtils
import com.shortdrama.movie.views.activities.play_movie.adapter.PlayMovieSpeedAdapter
import com.shortdrama.movie.views.bases.BaseBottomSheetDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class PlaySpeedMovieDialog(
    val activity: Activity,
    val currentSpeed: Int,
    val onClickItem: (PlayMovieSpeedModel) -> Unit
) : BaseBottomSheetDialog<DialogPlaySpeedMovieBinding>(activity) {
    private var playMovieSpeedAdapter: PlayMovieSpeedAdapter? = null

    override fun initViews() {
        super.initViews()
        playMovieSpeedAdapter = PlayMovieSpeedAdapter(activity) { item ->
            onClickItem(item)
            dismiss()
        }
        mBinding.rvPlaySpeed.adapter = playMovieSpeedAdapter
        playMovieSpeedAdapter?.submitData(DataUtils.listPlaySpeedMovie, currentSpeed)
    }

    override fun getLayoutDialog(): Int = R.layout.dialog_play_speed_movie

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
    }
}