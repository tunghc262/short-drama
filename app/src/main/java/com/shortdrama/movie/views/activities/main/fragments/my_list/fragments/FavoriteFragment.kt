package com.shortdrama.movie.views.activities.main.fragments.my_list.fragments

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.FragmentFavoriteBinding
import com.shortdrama.movie.views.activities.main.fragments.my_list.adapter.FavouriteAdapter
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.FavoriteViewModel
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.RemoveFavouriteDialog
import kotlinx.coroutines.launch

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    private var favouriteAdapter: FavouriteAdapter? = null
    private val viewModel: FavoriteViewModel by activityViewModels()

    override fun getLayoutFragment(): Int = R.layout.fragment_favorite
    override fun initViews() {
        super.initViews()
        activity?.let { act ->
            favouriteAdapter = FavouriteAdapter(act) {
                RemoveFavouriteDialog(act) {
                    viewModel.removeFromFavourite(it.id)
                }.show()
            }
        }
        mBinding.rvMyFavourite.adapter = favouriteAdapter
    }
    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteMovies.collect { list ->
                    Log.e("MOVIE", "observerData: favourite: ${list.size}")
                    if (list.isNotEmpty()) {
                        mBinding.rvMyFavourite.visibleView()
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
                        favouriteAdapter?.submitData(movies)
                    } else {
                        mBinding.rvMyFavourite.goneView()
                        mBinding.llEmpty.visibleView()
                    }
                }
            }
        }
    }
}