package com.shortdrama.movie.views.activities.main.fragments.my_list

import android.content.Intent
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.widget.ViewPager2
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.FragmentMyListBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeViewPager
import com.shortdrama.movie.views.activities.main.fragments.my_list.fragments.FavoriteFragment
import com.shortdrama.movie.views.activities.main.fragments.my_list.fragments.HistoryFragment
import com.shortdrama.movie.views.activities.search.SearchActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setTextColorById
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyListFragment : BaseFragment<FragmentMyListBinding>() {
    private var isMyFavourite: Boolean = true
    private var isShowDelete: Boolean = false
    private var homePagerAdapter: HomeViewPager? = null
    override fun getLayoutFragment(): Int = R.layout.fragment_my_list
    override fun initViews() {
        super.initViews()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        activity?.let { act ->
            homePagerAdapter = HomeViewPager(act)
            homePagerAdapter?.submitFragments(
                listOf(
                    FavoriteFragment(),
                    HistoryFragment()
                )
            )
            mBinding.vpMainForYou.apply {
                adapter = homePagerAdapter
                offscreenPageLimit = 1
                isUserInputEnabled = false
                animation = null
            }
            mBinding.vpMainForYou.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    resetTab()
                    when (position) {
                        0 -> setUpTabFavourite()
                        1 -> setUpTabHistory()
                    }
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
        }
    }

    private fun setUpTabFavourite() {
        activity?.let { act ->
            isMyFavourite = true
            mBinding.tvTabFavorite.setTextColorById(R.color.white)
            mBinding.tvTabFavorite.setBackgroundResource(R.drawable.bg_btn_fill_24)
            val typeface = ResourcesCompat.getFont(act, R.font.poppins_medium)
            mBinding.tvTabFavorite.typeface = typeface
        }
    }

    private fun setUpTabHistory() {
        activity?.let { act ->
            isMyFavourite = false
            mBinding.tvTabHistory.setTextColorById(R.color.white)
            mBinding.tvTabHistory.setBackgroundResource(R.drawable.bg_btn_fill_24)
            val typeface = ResourcesCompat.getFont(act, R.font.poppins_medium)
            mBinding.tvTabHistory.typeface = typeface
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvTabFavorite.onClickAlpha {
            if (isMyFavourite) return@onClickAlpha
            mBinding.vpMainForYou.currentItem = 0
            resetTab()
            setUpTabFavourite()
        }
        mBinding.tvTabHistory.onClickAlpha {
            if (!isMyFavourite) return@onClickAlpha
            mBinding.vpMainForYou.currentItem = 1
            resetTab()
            setUpTabHistory()
        }

        mBinding.llSearch.onClickAlpha {
            activity?.let { act ->
                val intent = Intent(act, SearchActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun resetTab() {
        activity?.let { act ->
            mBinding.tvTabFavorite.setTextColorById(R.color.color_818181)
            mBinding.tvTabHistory.setTextColorById(R.color.color_818181)
            mBinding.tvTabFavorite.background = null
            mBinding.tvTabHistory.background = null
            val typeface = ResourcesCompat.getFont(act, R.font.poppins_regular)
            mBinding.tvTabFavorite.typeface = typeface
            mBinding.tvTabHistory.typeface = typeface
        }
    }
}