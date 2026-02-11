package com.shortdrama.movie.views.activities.see_more.popular

import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.NetworkUtils
import com.module.ads.utils.PurchaseUtils
import com.module.core_api_storage.model_ui.DramaUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivitySeeMorePopularBinding
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.activities.search.SearchActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeMorePopularActivity : BaseActivity<ActivitySeeMorePopularBinding>() {
    private var seeMorePopularAdapter: SeeMorePopularAdapter? = null
    private var listPopular: MutableList<DramaWithGenresUIModel> = mutableListOf()
    override fun getLayoutActivity(): Int = R.layout.activity_see_more_popular
    override fun initViews() {
        super.initViews()
        if (intent.hasExtra("POPULAR_LIST")) {
            val list = intent.getParcelableArrayListExtra<DramaWithGenresUIModel>("POPULAR_LIST")
            list?.let {
                listPopular.clear()
                listPopular.addAll(it)
            }
        }
        initAdapter()
    }

    @OptIn(UnstableApi::class)
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
            ) && listPopular.size >= 2
        ) {
            listPopular.add(
                1, DramaWithGenresUIModel(
                    isAds = true,
                    dramaUIModel = DramaUIModel(
                        dramaId = "",
                        dramaName = "",
                        dramaDescription = "",
                        dramaThumb = "",
                        dramaTrailer = "",
                        totalEpisode = ""
                    ),
                    dramaGenresUIModel = emptyList(),
                )
            )
        }
        if (listPopular.isNotEmpty()) {
            seeMorePopularAdapter?.submitData(listPopular)
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivBack.onClickAlpha {
            finish()
        }
        mBinding.btnSearch.onClickAlpha {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }
}