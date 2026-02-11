package com.shortdrama.movie.views.activities.see_more.coming_soon

import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.NetworkUtils
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ActivitySeeMoreComingSoonBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieComingSoonAdapter
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeMoreComingSoonActivity : BaseActivity<ActivitySeeMoreComingSoonBinding>() {
    private var seeMoreComingSoonAdapter: MovieComingSoonAdapter? = null
    private var listPopular: MutableList<TVSeriesUiModel> = mutableListOf()
    override fun getLayoutActivity(): Int = R.layout.activity_see_more_coming_soon
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
        seeMoreComingSoonAdapter = MovieComingSoonAdapter(
            isShowDateOriginal = false,
            iShowDateNumeric = true
        ) { movie ->

        }
        mBinding.rvItemCate.adapter = seeMoreComingSoonAdapter
        seeMoreComingSoonAdapter?.submitData(listPopular)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivBack.onClickAlpha {
            finish()
        }
    }
}