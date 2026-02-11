package com.shortdrama.movie.views.activities.main.fragments.my_list.fragments

import android.content.Intent
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.module.ads.admob.inters.IntersInApp
import com.module.core_api_storage.model_ui.DramaGenresUIModel
import com.module.core_api_storage.model_ui.DramaUIModel
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentFavoriteBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.activities.main.fragments.my_list.adapter.FavouriteAdapter
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.FavoriteViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
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
            favouriteAdapter = FavouriteAdapter(
                act,
                onClickItem = { movie ->
                    activity?.let { act ->
                        IntersInApp.getInstance().showAds(act) {
                            val intent = Intent(act, PlayMovieActivity::class.java)
                            intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                            startActivity(intent)
                        }
                    }
                },
                onClickRemoveItem = {
                    RemoveFavouriteDialog(act) {
                        viewModel.removeFromFavourite(it.dramaUIModel.dramaId)
                        SharePrefUtils.putBoolean(it.dramaUIModel.dramaId, false)
                    }.show()
                }
            )
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
                            DramaWithGenresUIModel(
                                dramaUIModel = DramaUIModel(
                                    dramaId = it.dramaId,
                                    dramaName = it.name,
                                    dramaDescription = it.description,
                                    dramaThumb = it.thumb,
                                    dramaTrailer = it.dramaTrailer,
                                    totalEpisode = it.totalEpisode
                                ),
                                dramaGenresUIModel = Gson().fromJson<List<DramaGenresUIModel>>(
                                    it.genresJson,
                                    object : TypeToken<List<DramaGenresUIModel>>() {}.type
                                ) ?: emptyList(),
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