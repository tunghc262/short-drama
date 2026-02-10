package com.shortdrama.movie.views.activities.see_more

import android.content.Intent
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.NetworkUtils
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivitySeeMorePopularBinding
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeMorePopularActivity : BaseActivity<ActivitySeeMorePopularBinding>() {
    private var seeMorePopularAdapter: SeeMorePopularAdapter? = null
    private var listPopular: MutableList<TVSeriesUiModel> = mutableListOf()
    override fun getLayoutActivity(): Int = R.layout.activity_see_more_popular
    override fun initViews() {
        super.initViews()
        if (intent.hasExtra("POPULAR_LIST")) {
            val list = intent.getParcelableArrayListExtra<TVSeriesUiModel>("POPULAR_LIST")
            list?.let {
                listPopular.clear()
                listPopular.addAll(it)
            }
        }
        initAdapter()
    }

    private fun initAdapter() {
        seeMorePopularAdapter = SeeMorePopularAdapter(this) { movie ->
            IntersInApp.getInstance().showAds(this) {
                val intent = Intent(this, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                startActivity(intent)
            }
        }
        mBinding.rvItemCate.adapter = seeMorePopularAdapter
        if (FirebaseQuery.getEnableAds() && !PurchaseUtils.isNoAds(this) && NetworkUtils.isNetwork(
                this
            )
        ) {
            listPopular.add(
                1, TVSeriesUiModel(
                    isAds = true
                )
            )
        }
        seeMorePopularAdapter?.submitData(listPopular)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivBack.onClickAlpha {
            finish()
        }
    }
}