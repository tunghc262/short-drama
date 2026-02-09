package com.shortdrama.movie.views.activities.main.fragments.for_you

import android.content.Intent
import androidx.core.view.WindowCompat
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.module.ads.admob.inters.IntersInApp
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.entity.MovieFavoriteEntity
import com.shortdrama.movie.databinding.FragmentForyouBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.ForYouViewPager
import com.shortdrama.movie.views.activities.main.fragments.my_list.viewmodel.FavoriteViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.dialogs.EpisodesMovieDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForYouFragment : BaseFragment<FragmentForyouBinding>() {
    private var mainForYouAdapter: ForYouViewPager? = null
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun getLayoutFragment(): Int = R.layout.fragment_foryou
    override fun initViews() {
        super.initViews()
        initViewPager()
    }

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
                        EpisodesMovieDialog(
                            act,
                            FirebaseQuery.getNumberLockMovie(),
                            1,
                            obj,
                            onClickItemEpisode = { episodeSerialId ->
                                val intent = Intent(act, PlayMovieActivity::class.java)
                                intent.putExtra(AppConstants.OBJ_MOVIE, obj)
                                intent.putExtra(AppConstants.EPISODE_MOVIE, episodeSerialId)
                                startActivity(intent)
                            },
                            onClickMovie = { objMovie ->
                                val intent = Intent(act, PlayMovieActivity::class.java)
                                intent.putExtra(AppConstants.OBJ_MOVIE, objMovie)
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
//            mBinding.vpMainForYou.post {
//                mainForYouAdapter?.playAt(0)
//            }
            mBinding.vpMainForYou.registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
//                        if (position == 4) {
//                            mBinding.llSearch.goneView()
//                            return
//                        } else {
//                            mBinding.llSearch.visibleView()
//                        }

                        val item = mainForYouAdapter?.getItemAt(position)
                        when (item) {
                            is ForYouItem.Movie -> {
                                mBinding.llSearch.visibleView()
                                mBinding.vpMainForYou.post {
                                    mainForYouAdapter?.playAt(position)
                                }
                            }

                            is ForYouItem.Ads -> {
                                mBinding.llSearch.goneView()
                                mainForYouAdapter?.clearPlayItemAds(position)
                            }

                            else -> Unit
                        }
//                        mBinding.vpMainForYou.post {
//                            mainForYouAdapter?.playAt(position)
//                        }
                    }
                }
            )
        }
    }

    override fun onClickViews() {
        super.onClickViews()
    }

}