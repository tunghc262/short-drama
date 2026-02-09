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
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHistotyBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.MovieNewReleaseAdapter
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
    private var historyAdapter: MovieNewReleaseAdapter? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_histoty

    @OptIn(UnstableApi::class)
    override fun initViews() {
        super.initViews()
        historyAdapter = MovieNewReleaseAdapter { movie ->
            activity?.let { act ->
                val intent = Intent(act, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                startActivity(intent)
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
                        val movies = list.map {
                            TVSeriesUiModel(
                                id = it.id,
                                name = it.name,
                                originalName = it.originalName,
                                overview = it.overview,
                                numberOfSeasons = it.numberOfSeasons,
                                numberOfEpisodes = it.numberOfEpisodes,
                                posterPath = it.posterPath,
                                genres = it.genres
                            )
                        }
                        historyAdapter?.submitData(movies)
                    } else {
                        mBinding.rvMyHistory.goneView()
                        mBinding.llEmpty.visibleView()
                    }
                }
            }
        }
    }
}