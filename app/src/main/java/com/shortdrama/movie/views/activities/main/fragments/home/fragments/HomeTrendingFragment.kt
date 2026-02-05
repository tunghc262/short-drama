package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.FragmentHomeTrendingBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeTrendingBannerAdapter
import com.shortdrama.movie.views.bases.BaseFragment


class HomeTrendingFragment : BaseFragment<FragmentHomeTrendingBinding>() {
    private var homeTrendingBannerAdapter: HomeTrendingBannerAdapter? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_home_trending
    override fun initViews() {
        super.initViews()
        setUpBannerTrending()
    }

    private fun setUpBannerTrending() {
        activity?.let { act ->
            val listBanner = ArrayList<Int>()
            listBanner.add(R.drawable.img_placeholder)
            listBanner.add(R.drawable.img_no_internet)
            listBanner.add(R.drawable.img_on_board_1)
            homeTrendingBannerAdapter =
                HomeTrendingBannerAdapter(act, onClickItem = { obj ->
//                    val intent = Intent(act, PlayMovieActivity::class.java)
//                    intent.putExtra(AppConstants.OBJ_MOVIE, obj)
//                    startActivity(intent)
                })
            mBinding.vpBannerTrending.adapter = homeTrendingBannerAdapter
            homeTrendingBannerAdapter?.submitData(listBanner)

            mBinding.vpBannerTrending.clipToPadding = false
            mBinding.vpBannerTrending.clipChildren = false
            mBinding.vpBannerTrending.offscreenPageLimit = 3
            mBinding.dotIndicators.attachTo(mBinding.vpBannerTrending)
        }
    }

    override fun onClickViews() {
        super.onClickViews()
    }
}
