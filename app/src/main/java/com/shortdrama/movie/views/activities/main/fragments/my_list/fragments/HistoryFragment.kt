package com.shortdrama.movie.views.activities.main.fragments.my_list.fragments

import android.content.Intent
import android.util.Log
import androidx.annotation.OptIn
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.inters.IntersInApp
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHistotyBinding
import com.shortdrama.movie.views.activities.main.fragments.my_list.adapter.HistoryMovieAdapter
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.HistoryViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistotyBinding>() {
    private val viewModel: HistoryViewModel by activityViewModels()
    private var historyAdapter: HistoryMovieAdapter? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_histoty

    @OptIn(UnstableApi::class)
    override fun initViews() {
        super.initViews()
        historyAdapter = HistoryMovieAdapter { obj ->
            activity?.let { act ->
                val movies = TVSeriesUiModel(
                    id = obj.id,
                    name = obj.name,
                    originalName = obj.originalName,
                    overview = obj.overview,
                    numberOfSeasons = obj.numberOfSeasons,
                    numberOfEpisodes = obj.numberOfEpisodes,
                    posterPath = obj.posterPath,
                    genres = obj.genres
                )
                activity?.let { act ->
                    IntersInApp.getInstance().showAds(act) {
                        val intent = Intent(act, PlayMovieActivity::class.java)
                        intent.putExtra(AppConstants.OBJ_MOVIE, movies)
                        intent.putExtra(AppConstants.CURRENT_EPISODE_MOVIE_ID, obj.episodeCurrentId)
                        startActivity(intent)
                    }
                }
            }
        }
        mBinding.rvMyHistory.adapter = historyAdapter
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.watchHistory.collect { list ->
                    Log.e("MOVIE", "observerData: history: ${list.size}")
                    if (list.isNotEmpty()) {
                        mBinding.rvMyHistory.visibleView()
                        mBinding.llEmpty.goneView()
                        historyAdapter?.submitData(list)
                    } else {
                        mBinding.rvMyHistory.goneView()
                        mBinding.llEmpty.visibleView()
                    }
                }
            }
        }
    }
}