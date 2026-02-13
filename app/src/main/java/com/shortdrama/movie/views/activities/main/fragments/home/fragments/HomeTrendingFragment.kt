package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.GridLayoutManager
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.admob.natives.NativeInAppAll
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeTrendingBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeTrendingBannerAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieNewReleaseAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MoviePopularAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieTopChartAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeTrendingViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.activities.see_more.popular.SeeMorePopularActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setHorizontalNestedScrollFix
import com.shortdrama.movie.views.bases.ext.visibleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeTrendingFragment : BaseFragment<FragmentHomeTrendingBinding>() {
    private var homeTrendingBannerAdapter: HomeTrendingBannerAdapter? = null
    private var moviePopularAdapter: MoviePopularAdapter? = null

    private var movieTopChartAdapter: MovieTopChartAdapter? = null

    private var listPopular: MutableList<DramaWithGenresUIModel> = mutableListOf()

    private var newReleaseAdapter: MovieNewReleaseAdapter? = null
    private val viewModel: HomeTrendingViewModel by activityViewModels()
    override fun getLayoutFragment(): Int = R.layout.fragment_home_trending
    override fun initViews() {
        super.initViews()
        activity?.let { act ->
            NativeInAppAll.getInstance().loadAndShow(
                act,
                mBinding.lnNative,
                FirebaseQuery.getIdNativeMain(),
                object : CallbackNative {
                    override fun onLoaded() {
                        mBinding.lnNative.visibleView()
                    }

                    override fun onFailed() {
                        mBinding.lnNative.goneView()
                    }

                    override fun onAdImpression() {
                    }
                },
                3
            )
        }
        Log.e("TAG", "init HomeTrendingFragment")
        setUpBannerTrending()
        setUpRecyclerView()
        viewModel.loadMovieBanner()
        viewModel.loadMoviePopular()
        viewModel.loadMovieNewRelease()
        viewModel.loadMovieTopChart()
    }

    private fun setUpRecyclerView() {
        activity?.let { act ->
            moviePopularAdapter = MoviePopularAdapter(act, onClickItem = { movie ->
                goToWatchMovie(movie)
            })
            mBinding.rvPopularMovie.apply {
                adapter = moviePopularAdapter
                layoutManager = GridLayoutManager(act, 2)
            }

            newReleaseAdapter = MovieNewReleaseAdapter(act, onClickItem = { movie ->
                goToWatchMovie(movie)
            })
            mBinding.rvNewRelease.apply {
                adapter = newReleaseAdapter
            }
            mBinding.rvNewRelease.setHorizontalNestedScrollFix()

            movieTopChartAdapter = MovieTopChartAdapter(act, onClickItem = { obj ->
                goToWatchMovie(obj)
            })

            val layoutManager = GridLayoutManager(act, 2, GridLayoutManager.HORIZONTAL, false)

            mBinding.rvTopChart.apply {
                this.layoutManager = layoutManager
                this.adapter = movieTopChartAdapter
            }
            mBinding.rvTopChart.setHorizontalNestedScrollFix()
        }
    }

    private fun setUpBannerTrending() {
        activity?.let { act ->
            homeTrendingBannerAdapter =
                HomeTrendingBannerAdapter(act, onClickItem = { obj ->
                    goToWatchMovie(obj)
                })
            mBinding.vpBannerTrending.adapter = homeTrendingBannerAdapter
            mBinding.vpBannerTrending.clipToPadding = false
            mBinding.vpBannerTrending.clipChildren = false
            mBinding.vpBannerTrending.offscreenPageLimit = 3
            mBinding.dotIndicators.attachTo(mBinding.vpBannerTrending)
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.btnNextPopular.onClickAlpha {
            activity?.let { act ->
                val intent = Intent(act, SeeMorePopularActivity::class.java)
                intent.putParcelableArrayListExtra("POPULAR_LIST", ArrayList(listPopular))
                startActivity(intent)
            }
        }
    }

    override fun observerData() {
        super.observerData()
        //banner
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.banner.collect { list ->
                if (list.isNotEmpty()) {
                    homeTrendingBannerAdapter?.submitData(list)
                }
            }
        }

        //popular
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.popular.collect { list ->
                if (list.isNotEmpty()) {
                    listPopular.clear()
                    listPopular.addAll(list)
                    moviePopularAdapter?.submitData(list)
                }
            }
        }
        // new release
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.newRelease.collect { list ->
                if (list.isNotEmpty()) {
                    newReleaseAdapter?.submitData(list)
                }
            }
        }
        // top chart
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.topChart.collect { list ->
                if (list.isNotEmpty()) {
                    movieTopChartAdapter?.submitData(list)
                }
            }
        }
    }

    @OptIn(UnstableApi::class)
    private fun goToWatchMovie(movie: DramaWithGenresUIModel) {
        activity?.let { act ->
            IntersInApp.getInstance().showAds(act) {
                val intent = Intent(act, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                startActivity(intent)
            }
        }
    }
}
