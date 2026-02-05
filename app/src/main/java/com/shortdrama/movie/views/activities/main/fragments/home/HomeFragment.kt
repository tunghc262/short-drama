package com.shortdrama.movie.views.activities.main.fragments.home

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.FragmentHomeBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeViewPager
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.TabHomeAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.fragments.HomeCategoryFragment
import com.shortdrama.movie.views.activities.main.fragments.home.fragments.HomeNewFragment
import com.shortdrama.movie.views.activities.main.fragments.home.fragments.HomeRankingFragment
import com.shortdrama.movie.views.activities.main.fragments.home.fragments.HomeTrendingFragment
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.customs.ViewCommon


class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private var tabHomeAdapter: TabHomeAdapter? = null;
    private var homeViewPager: HomeViewPager? = null

    override fun getLayoutFragment(): Int = R.layout.fragment_home
    override fun initViews() {
        super.initViews()
        setUpTabHome()
        setUpViewPager()
    }

    private fun setUpViewPager() {
        activity?.let { act ->
            homeViewPager = HomeViewPager(act)
            homeViewPager?.submitFragments(
                listOf(
                    HomeTrendingFragment(),
                    HomeNewFragment(),
                    HomeRankingFragment(),
                    HomeCategoryFragment()
                )
            )
            mBinding.vpHome.adapter = homeViewPager
            mBinding.vpHome.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tabHomeAdapter?.setSelectedItem(position)
                    ViewCommon.scrollToCenter(act, position, mBinding.rvCateHome)
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageScrollStateChanged(state: Int) {
                    when (state) {
                        ViewPager2.SCROLL_STATE_IDLE -> {}
                        ViewPager2.SCROLL_STATE_DRAGGING -> {}
                        ViewPager2.SCROLL_STATE_SETTLING -> {}
                    }
                }
            })
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        activity?.let { act ->
            mBinding.llSearch.onClickAlpha {
//                val intent = Intent(act, MovieSearchActivity::class.java)
//                startActivity(intent)
            }
        }
    }

    private fun setUpTabHome() {
        activity?.let { act ->
            val listCate = listOf(
                getString(R.string.trending),
                getString(R.string.news),
                getString(R.string.ranking),
                getString(R.string.category)
            )
            tabHomeAdapter = TabHomeAdapter(act, onClickItem = { pos ->
                ViewCommon.scrollToCenter(act, pos, mBinding.rvCateHome)
                mBinding.vpHome.setCurrentItem(pos, false)
            })
            mBinding.rvCateHome.apply {
                layoutManager = LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false)
                adapter = tabHomeAdapter
            }
            tabHomeAdapter?.submitData(listCate)
        }
    }
}