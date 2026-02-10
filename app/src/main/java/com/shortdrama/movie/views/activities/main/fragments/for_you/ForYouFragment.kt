package com.shortdrama.movie.views.activities.main.fragments.for_you

import android.content.Intent
import androidx.annotation.OptIn
import androidx.core.view.WindowCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.widget.ViewPager2
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import com.shortdrama.movie.data.models.ForYouModel
import com.shortdrama.movie.databinding.FragmentForyouBinding
import com.shortdrama.movie.utils.ShareUtils
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.ForYouViewPager
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.FavoriteViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.insertAdsEvery4Items
import com.shortdrama.movie.views.bases.ext.insertNormal
import com.shortdrama.movie.views.bases.ext.visibleView
import com.shortdrama.movie.views.dialogs.EpisodesForYouDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ForYouFragment : BaseFragment<FragmentForyouBinding>() {
    private var mainForYouAdapter: ForYouViewPager? = null
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val forYouViewModel: ForYouViewModel by activityViewModels()
    override fun getLayoutFragment(): Int = R.layout.fragment_foryou
    override fun initViews() {
        super.initViews()
        initViewPager()
    }

    @OptIn(UnstableApi::class)
    private fun initViewPager() {
        activity?.let { act ->
            WindowCompat.setDecorFitsSystemWindows(act.window, false)
            mainForYouAdapter = ForYouViewPager(
                act,
                onClickWatchNow = { obj ->
                    IntersInApp.getInstance().showAds(act) {
                        val intent = Intent(act, PlayMovieActivity::class.java)
                        intent.putExtra(AppConstants.OBJ_MOVIE, obj)
                        startActivity(intent)
                    }
                },
                onClickItemSaveMovie = { obj ->
                    val favouriteMovieEntity = MovieFavoriteEntity(
                        id = obj.id,
                        name = obj.name,
                        originalName = obj.originalName,
                        overview = obj.overview,
                        numberOfSeasons = obj.numberOfSeasons,
                        numberOfEpisodes = obj.numberOfEpisodes,
                        posterPath = obj.posterPath,
                        genres = obj.genres
                    )
                    favoriteViewModel.addToFavourite(favouriteMovieEntity)
                },
                onClickItemUnSaveMovie = { obj ->
                    favoriteViewModel.removeFromFavourite(obj.id)
                },
                onClickItemEpisodes = { obj ->
                    val episodeBottomDialog =
                        EpisodesForYouDialog(
                            act,
                            FirebaseQuery.getNumberLockMovie().toInt(),
                            1,
                            obj,
                            onClickItemEpisode = { episodeSerialId ->
                                val intent = Intent(act, PlayMovieActivity::class.java)
                                intent.putExtra(AppConstants.OBJ_MOVIE, obj)
                                intent.putExtra(
                                    AppConstants.CURRENT_EPISODE_MOVIE_ID,
                                    episodeSerialId
                                )
                                startActivity(intent)
                            })
                    episodeBottomDialog.show(parentFragmentManager, "EpisodeBottomDialog")
                },
                onClickItemShare = {
                    ShareUtils.shareApp(act)
                })
            mBinding.vpMainForYou.apply {
                adapter = mainForYouAdapter
                offscreenPageLimit = 1
            }
            mBinding.vpMainForYou.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        val item = mainForYouAdapter?.getItemAt(position)
                        when (item) {
                            is ForYouModel.Movie -> {
                                mBinding.llSearch.visibleView()
                                mBinding.vpMainForYou.post {
                                    mainForYouAdapter?.playAt(position)
                                }
                            }

                            is ForYouModel.Ads -> {
                                mBinding.llSearch.goneView()
                                mainForYouAdapter?.clearPlayItemAds(position)
                            }

                            else -> Unit
                        }
                    }
                }
            )
        }
    }

    override fun onClickViews() {
        super.onClickViews()
    }

    override fun observerData() {
        super.observerData()
        activity?.let { act ->
            if (!PurchaseUtils.isNoAds(act) && FirebaseQuery.getEnableAds()) {
                lifecycleScope.launch {
                    forYouViewModel.tvSeriesPagingFlow
                        .insertAdsEvery4Items()
                        .collectLatest { list ->
                            mainForYouAdapter?.submitData(list)
                        }
                }
            } else {
                lifecycleScope.launch {
                    forYouViewModel.tvSeriesPagingFlow
                        .insertNormal()
                        .collectLatest { list ->
                            mainForYouAdapter?.submitData(list)
                        }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainForYouAdapter?.pause()
    }

    override fun onDestroyView() {
        mainForYouAdapter?.releasePlayer()
        super.onDestroyView()
    }

}