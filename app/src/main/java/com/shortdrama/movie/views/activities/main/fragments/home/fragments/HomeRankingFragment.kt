package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.module.ads.admob.inters.IntersInApp
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeRankingBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeRankingAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeRankingViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeRankingFragment : BaseFragment<FragmentHomeRankingBinding>() {

    private var homeRankingAdapter: HomeRankingAdapter? = null
    private val viewModel: HomeRankingViewModel by activityViewModels()

    override fun getLayoutFragment(): Int = R.layout.fragment_home_ranking
    override fun initViews() {
        super.initViews()
        activity?.let { act ->
            homeRankingAdapter = HomeRankingAdapter(act) { movie ->
                activity?.let { act ->
                    IntersInApp.getInstance().showAds(act) {
                        val intent = Intent(act, PlayMovieActivity::class.java)
                        intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                        startActivity(intent)
                    }
                }
            }
            mBinding.rcvRanking.adapter = homeRankingAdapter
        }
        viewModel.loadRanking(65)
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ranking.collect { list ->
                    if (list.isNotEmpty()) {
                        mBinding.llEmpty.goneView()
                        mBinding.rcvRanking.visibleView()
                        homeRankingAdapter?.submitData(list)
                    } else {
                        mBinding.llEmpty.visibleView()
                        mBinding.rcvRanking.goneView()
                    }
                }
            }
        }
    }
}
