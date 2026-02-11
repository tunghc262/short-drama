package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.module.ads.admob.inters.IntersInApp
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeNewBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeCategoryAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieComingSoonAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieExclusiveAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeNewViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.activities.see_more.coming_soon.SeeMoreComingSoonActivity
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
    private var listComingSoon: MutableList<DramaWithGenresUIModel> = mutableListOf()
    private var comingSoonAdapter: MovieComingSoonAdapter? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_home_new
    override fun initViews() {
        super.initViews()
        initAdapter()
        viewModel.loadNewRelease()
        viewModel.loadComingSoon()
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
        mBinding.btnNextComingSoon.onClickAlpha {
            activity?.let { act ->
                val intent = Intent(act, SeeMoreComingSoonActivity::class.java)
                intent.putParcelableArrayListExtra("POPULAR_LIST", ArrayList(listComingSoon))
                startActivity(intent)
            }
        }
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exclusive.collect { list ->
                    exclusiveAdapter?.submitData(list)
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newRelease.collect { list ->

                    newReleaseAdapter?.submitData(list)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.comingSoon.collect { list ->
                    listComingSoon.clear()
                    listComingSoon.addAll(list)
                    comingSoonAdapter?.submitData(list)
                }
            }
        }
    }

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
