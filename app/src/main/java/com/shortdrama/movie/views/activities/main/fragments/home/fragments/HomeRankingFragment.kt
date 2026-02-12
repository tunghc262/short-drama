package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.NetworkUtils
import com.module.ads.utils.PurchaseUtils
import com.module.core_api_storage.model_ui.DramaUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
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
            homeRankingAdapter = HomeRankingAdapter(act, false) { movie ->
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
        viewModel.loadRanking()
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ranking.collect { list ->
                    val listMovie = list.toMutableList()
                    if (listMovie.isNotEmpty()) {
                        mBinding.llEmpty.goneView()
                        mBinding.rcvRanking.visibleView()
                        activity?.let { act ->
                            if (FirebaseQuery.getEnableAds() && !PurchaseUtils.isNoAds(act) && NetworkUtils.isNetwork(
                                    act
                                ) && listMovie.size >= 2
                            ) {
                                listMovie.add(
                                    2, DramaWithGenresUIModel(
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
                        }
                        homeRankingAdapter?.submitData(listMovie)
                    } else {
                        mBinding.llEmpty.visibleView()
                        mBinding.rcvRanking.goneView()
                    }
                }
            }
        }
    }
}
