package com.shortdrama.movie.views.activities.main.fragments.home.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentHomeCategoryBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.HomeCategoryAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.TabCategoryAdapter
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeCategoryViewModel
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.setHorizontalNestedScrollFix
import com.shortdrama.movie.views.customs.ViewCommon
import com.shortdrama.movie.views.dialogs.CategoryTabDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeCategoryFragment : BaseFragment<FragmentHomeCategoryBinding>() {
    private var tabCategoryAdapter: TabCategoryAdapter? = null
    private var categoryAdapter: HomeCategoryAdapter? = null

    private var listMovie = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart1 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart2 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart3 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart4 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart5 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart6 = mutableListOf<TVSeriesUiModel>()
    private var listMoviePart7 = mutableListOf<TVSeriesUiModel>()

    private var currentIndexTab = 0

    private val viewModel: HomeCategoryViewModel by activityViewModels()

    override fun getLayoutFragment(): Int = R.layout.fragment_home_category
    override fun initViews() {
        super.initViews()
        initListCate()
        initTabCate()
        viewModel.loadCategory(45)
    }

    private fun initListCate() {
        activity?.let { act ->
            categoryAdapter = HomeCategoryAdapter { obj ->
                val intent = Intent(act, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, obj)
                startActivity(intent)
            }
            mBinding.rvMovieCategories.adapter = categoryAdapter
        }
    }

    private fun initTabCate() {
        activity?.let { act ->
            val listCate = listOf(
                "All",
                "Romantic",
                "Drama",
                "VIP",
                "Men",
                "18+",
                "School",
                "Porn"
            )
            tabCategoryAdapter = TabCategoryAdapter(act, onClickItem = { pos ->
                currentIndexTab = pos
                when (pos) {
                    0 -> categoryAdapter?.submitData(listMovie)
                    1 -> categoryAdapter?.submitData(listMoviePart1)
                    2 -> categoryAdapter?.submitData(listMoviePart2)
                    3 -> categoryAdapter?.submitData(listMoviePart3)
                    4 -> categoryAdapter?.submitData(listMoviePart4)
                    5 -> categoryAdapter?.submitData(listMoviePart5)
                    6 -> categoryAdapter?.submitData(listMoviePart6)
                    7 -> categoryAdapter?.submitData(listMoviePart7)
                }
            })
            mBinding.rvCateCategories.apply {
                adapter = tabCategoryAdapter
                setHorizontalNestedScrollFix()
            }
            tabCategoryAdapter?.submitData(listCate)

        }
    }

    override fun onClickViews() {
        super.onClickViews()
        activity?.let { act ->
            mBinding.ivDropTab.onClickAlpha {
                CategoryTabDialog(act, currentIndexTab) { index ->
                    when (index) {
                        0 -> categoryAdapter?.submitData(listMovie)
                        1 -> categoryAdapter?.submitData(listMoviePart1)
                        2 -> categoryAdapter?.submitData(listMoviePart2)
                        3 -> categoryAdapter?.submitData(listMoviePart3)
                        4 -> categoryAdapter?.submitData(listMoviePart4)
                        5 -> categoryAdapter?.submitData(listMoviePart5)
                        6 -> categoryAdapter?.submitData(listMoviePart6)
                        7 -> categoryAdapter?.submitData(listMoviePart7)
                    }
                    currentIndexTab = index
                    tabCategoryAdapter?.setSelectedItem(index)
                    ViewCommon.scrollToCenter(act, index, mBinding.rvCateCategories)
                }.show()
            }
        }
    }

    override fun observerData() {
        super.observerData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.category.collect { list ->
                    if (list.isNotEmpty()) {
                        val listPart = partitionList(list, 7)
                        listMovie = list.toMutableList()
                        listMoviePart1 = listPart[0].toMutableList()
                        listMoviePart2 = listPart[1].toMutableList()
                        listMoviePart3 = listPart[2].toMutableList()
                        listMoviePart4 = listPart[3].toMutableList()
                        listMoviePart5 = listPart[4].toMutableList()
                        listMoviePart6 = listPart[5].toMutableList()
                        listMoviePart7 = listPart[6].toMutableList()
                        categoryAdapter?.submitData(list)
                    }
                }
            }
        }
    }

    fun <T> partitionList(list: List<T>, parts: Int): List<List<T>> {
        if (parts <= 0) return emptyList()

        val partSize = list.size / parts
        val remainder = list.size % parts

        val result = mutableListOf<List<T>>()
        var index = 0

        for (i in 0 until parts) {
            val extra = if (i < remainder) 1 else 0
            val size = partSize + extra
            if (size > 0) {
                result.add(list.subList(index, index + size))
            } else {
                result.add(emptyList())
            }
            index += size
        }
        return result
    }

}
