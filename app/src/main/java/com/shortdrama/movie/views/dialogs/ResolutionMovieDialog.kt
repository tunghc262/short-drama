package com.shortdrama.movie.views.dialogs

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.ResolutionMovieModel
import com.shortdrama.movie.databinding.DialogResolutionMovieBinding
import com.shortdrama.movie.utils.DataUtils
import com.shortdrama.movie.views.activities.play_movie.adapter.ResolutionMovieAdapter
import com.shortdrama.movie.views.bases.BaseBottomSheetDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class ResolutionMovieDialog(
    val activity: Activity,
    private var currentResolutionIndex: Int,
    val onClickItem: (ResolutionMovieModel) -> Unit
) : BaseBottomSheetDialog<DialogResolutionMovieBinding>(activity) {
    private var resolutionMovieAdapter: ResolutionMovieAdapter? = null
    override fun getLayoutDialog(): Int = R.layout.dialog_resolution_movie

    override fun initViews() {
        super.initViews()
        resolutionMovieAdapter = ResolutionMovieAdapter { item ->
            onClickItem(item)
            dismiss()
        }
        mBinding.rvResolutionMovie.apply {
            adapter = resolutionMovieAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
        resolutionMovieAdapter?.submitListData(DataUtils.listResolution, currentResolutionIndex)

    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
    }

}