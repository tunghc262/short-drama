package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeTrendingBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeTrendingBannerAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieNewReleaseAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MoviePopularAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieTopChartAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeTrendingViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.setHorizontalNestedScrollFix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeTrendingFragment : BaseFragment<FragmentHomeTrendingBinding>() {
    private var homeTrendingBannerAdapter: HomeTrendingBannerAdapter? = null
    private var moviePopularAdapter: MoviePopularAdapter? = null

    private var movieTopChartAdapter: MovieTopChartAdapter? = null

    private var newReleaseAdapter: MovieNewReleaseAdapter? = null
    private val viewModel: HomeTrendingViewModel by activityViewModels()
    override fun getLayoutFragment(): Int = R.layout.fragment_home_trending
    override fun initViews() {
        super.initViews()
        Log.e("TAG", "init HomeTrendingFragment")
        setUpBannerTrending()
        setUpRecyclerView()
        viewModel.loadMovieBanner(68)
        viewModel.loadMoviePopular(60)
        viewModel.loadMovieNewRelease(61)
        viewModel.loadMovieTopChart(63)
    }

    private fun setUpRecyclerView() {
        activity?.let { act ->
            moviePopularAdapter = MoviePopularAdapter(onClickItem = { movie ->
                goToWatchMovie(movie)
            })
            mBinding.rvPopularMovie.apply {
                adapter = moviePopularAdapter
                layoutManager = GridLayoutManager(act, 2)
            }

            newReleaseAdapter = MovieNewReleaseAdapter(onClickItem = { movie ->
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
        homeTrendingBannerAdapter =
            HomeTrendingBannerAdapter(onClickItem = { obj ->
                goToWatchMovie(obj)
            })
        mBinding.vpBannerTrending.adapter = homeTrendingBannerAdapter
        mBinding.vpBannerTrending.clipToPadding = false
        mBinding.vpBannerTrending.clipChildren = false
        mBinding.vpBannerTrending.offscreenPageLimit = 3
        mBinding.dotIndicators.attachTo(mBinding.vpBannerTrending)
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()

        //banner
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.banner.collect { list ->
                    if (list.isNotEmpty()) {
                        if (list.size >= 6) {
                            val randomList = list.shuffled().take(6)
                            homeTrendingBannerAdapter?.submitData(randomList)
                        } else {
                            homeTrendingBannerAdapter?.submitData(list)
                        }
                    }
                }
            }
        }

        //popular
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popular.collect { list ->
                    if (list.isNotEmpty()) {
                        val randomList = list.shuffled().take(6)
                        moviePopularAdapter?.submitData(randomList)
                    }
                }
            }
        }
        // new release
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newRelease.collect { list ->
                    if (list.isNotEmpty()) {
                        newReleaseAdapter?.submitData(list)
                    }
                }
            }
        }
        // top chart
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topChart.collect { list ->
                    if (list.isNotEmpty()) {
                        val randomList = list.shuffled().take(6)
                        movieTopChartAdapter?.submitData(randomList)
                    }
                }
            }
        }
    }

    private fun goToWatchMovie(movie: TVSeriesUiModel) {
        activity?.let { act ->
            val intent = Intent(act, PlayMovieActivity::class.java)
            intent.putExtra(AppConstants.OBJ_MOVIE, movie)
            startActivity(intent)
        }
    }
}
