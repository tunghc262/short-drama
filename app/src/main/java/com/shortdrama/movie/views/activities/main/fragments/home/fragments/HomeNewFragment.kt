package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.inters.IntersInApp
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeNewBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeCategoryAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieComingSoonAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieExclusiveAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieNewReleaseAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeNewViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setHorizontalNestedScrollFix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeNewFragment : BaseFragment<FragmentHomeNewBinding>() {

    private val viewModel: HomeNewViewModel by activityViewModels()
    private var exclusiveAdapter: MovieExclusiveAdapter? = null
    private var newReleaseAdapter: HomeCategoryAdapter? = null

    private var comingSoonAdapter: MovieComingSoonAdapter? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_home_new
    override fun initViews() {
        super.initViews()
        initAdapter()
//        viewModel.loadExclusive(200)
        viewModel.loadNewRelease(61)
        viewModel.loadComingSoon(64)
    }

    private fun initAdapter() {
        exclusiveAdapter = MovieExclusiveAdapter {
            goToWatchMovie(it)
        }
        mBinding.rcvExclusive.adapter = exclusiveAdapter
        mBinding.rcvExclusive.setHorizontalNestedScrollFix()
        newReleaseAdapter = HomeCategoryAdapter(onClickItem = { movie ->
            goToWatchMovie(movie)
        })
        mBinding.rvNewRelease.apply {
            adapter = newReleaseAdapter
        }
        comingSoonAdapter = MovieComingSoonAdapter {

        }
        mBinding.rcvComingSoon.adapter = comingSoonAdapter
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.llTryNow.onClickAlpha {

        }
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exclusive.collect { list ->
                    if (list.isNotEmpty()) {
                        val randomList = list.shuffled().take(6)
                        exclusiveAdapter?.submitData(randomList)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newRelease.collect { list ->
                    if (list.isNotEmpty()) {
                        val randomList = list.shuffled().take(9)
                        newReleaseAdapter?.submitData(randomList)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.comingSoon.collect { list ->
                    if (list.isNotEmpty()) {
                        val randomList = list.shuffled().take(4)
                        comingSoonAdapter?.submitData(randomList)
                    }
                }
            }
        }
    }

    private fun goToWatchMovie(movie: TVSeriesUiModel) {
        activity?.let { act ->
            IntersInApp.getInstance().showAds(act) {
                val intent = Intent(act, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                startActivity(intent)
            }
        }
    }
}
