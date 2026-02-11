package com.shortdrama.movie.views.activities.search

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.module.ads.admob.inters.IntersInApp
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivitySearchBinding
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.activities.search.adapter.SearchAdapter
import com.shortdrama.movie.views.activities.search.adapter.TrendingSearchAdapter
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.visibleView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>() {
    private val viewModel: SearchViewModel by viewModels()
    private var searchAdapter: SearchAdapter? = null
    private var trendingSearchAdapter: TrendingSearchAdapter? = null
    override fun getLayoutActivity(): Int = R.layout.activity_search
    override fun initViews() {
        super.initViews()
        initRecyclerViewAllDrama()
        initRecyclerViewTrending()
        viewModel.loadAllDrama()
        viewModel.loadDramaTrending()
    }

    override fun onResume() {
        super.onResume()
        mBinding.edtSearch.post {
            mBinding.edtSearch.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE)
                    as InputMethodManager
            imm.showSoftInput(mBinding.edtSearch, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun initRecyclerViewAllDrama() {
        searchAdapter = SearchAdapter(this) { movie ->
            IntersInApp.getInstance().showAds(this) {
                val intent = Intent(this, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, movie)
                startActivity(intent)
            }
        }
        mBinding.rvSearchMovie.adapter = searchAdapter
    }

    private fun initRecyclerViewTrending() {
        trendingSearchAdapter = TrendingSearchAdapter {
            IntersInApp.getInstance().showAds(this) {
                val intent = Intent(this, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, it)
                startActivity(intent)
            }
        }
        mBinding.rvTrendingMovie.adapter = trendingSearchAdapter
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivBack.onClickAlpha {
            finish()
        }
        mBinding.edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = s?.toString().orEmpty()
                if (text.isNotEmpty()) {
                    mBinding.tvTrending.goneView()
                    mBinding.rvTrendingMovie.goneView()
                    mBinding.rvSearchMovie.visibleView()
                } else if (text.isEmpty()) {
                    mBinding.tvTrending.visibleView()
                    mBinding.rvTrendingMovie.visibleView()
                    mBinding.rvSearchMovie.goneView()
                }
                searchAdapter?.filter(text)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        mBinding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    override fun observerData() {
        super.observerData()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.dramaTrending.collect { list ->
                    trendingSearchAdapter?.submitData(list)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.drama.collect { list ->
                    searchAdapter?.submitData(list)
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mBinding.edtSearch.windowToken, 0)
    }
}