package com.shortdrama.movie.views.activities.recomend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.DialogRecommendMovieBinding
import com.shortdrama.movie.views.activities.main.fragments.home.viewmodel.HomeTrendingViewModel
import com.shortdrama.movie.views.activities.main.fragments.my_list.adapter.GenreFavoriteAdapter
import com.shortdrama.movie.views.activities.play_movie.PlayMovieActivity
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class RecommendDialog() : DialogFragment() {
    private var _binding: DialogRecommendMovieBinding? = null
    private val binding get() = _binding!!
    private var recommendMovieAdapter: RecommendAdapter? = null
    private var listTVSeries: List<DramaWithGenresUIModel> = mutableListOf()
    private var currentDrama: DramaWithGenresUIModel? = null
    private val viewModel: HomeTrendingViewModel by viewModels()
    private var genreAdapter: GenreFavoriteAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        WindowInsetsControllerCompat(
            requireActivity().window,
            requireActivity().window.decorView
        ).show(WindowInsetsCompat.Type.statusBars())

        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.black)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRecommendMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        onClickView()
        viewModel.loadMoviePopular()
        observerData()
    }

    private fun observerData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.popular.collect { list ->
                    if (list.isNotEmpty()) {
                        listTVSeries = list
                        recommendMovieAdapter?.submitData(list)
                    }
                }
            }
        }
    }

    private fun initView() {
        recommendMovieAdapter = RecommendAdapter(requireActivity())
        binding.vpRecommendMovie.apply {
            adapter = recommendMovieAdapter
            offscreenPageLimit = 3
        }
        setupViewPagerEffect()
        genreAdapter = GenreFavoriteAdapter()
        binding.rvGenre.adapter = genreAdapter
        binding.vpRecommendMovie.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                Log.e("MOVIEE", "onPageScrolled: $position")
                if (position in listTVSeries.indices) {
                    currentDrama = listTVSeries[position]
                    updateUi()
                }
            }
        })
    }

    private fun updateUi() {
        currentDrama?.let {
            binding.apply {
                tvMovieName.text = it.dramaUIModel.dramaName
                tvDescription.text = it.dramaUIModel.dramaDescription
                genreAdapter?.submitData(it.dramaGenresUIModel)
            }
        }
    }

    private fun setupViewPagerEffect() {
        val compositePageTransformer = CompositePageTransformer()
        val marginPx = (20 * resources.displayMetrics.density).toInt()
        compositePageTransformer.addTransformer(MarginPageTransformer(marginPx))
        compositePageTransformer.addTransformer { page, position ->
            val absPosition = abs(position)
            val scale = if (absPosition <= 0.5f) {
                1.18f - (absPosition * 0.6f)
            } else {
                0.9f - (absPosition - 0.5f) * 0.2f
            }.coerceIn(0.9f, 1.18f)
            page.scaleX = scale
            page.scaleY = scale
            val pageWidth = page.width
            val translationX = -position * (pageWidth * 0.2f)
            page.translationX = translationX
        }
        binding.vpRecommendMovie.setPageTransformer(compositePageTransformer)
    }

    @OptIn(UnstableApi::class)
    private fun onClickView() {
        binding.ivClose.onClickAlpha {
            dismiss()
        }
        binding.llButtonWatchMovie.onClickAlpha {
            currentDrama?.let {
                val intent = Intent(activity, PlayMovieActivity::class.java)
                intent.putExtra(AppConstants.OBJ_MOVIE, it)
                startActivity(intent)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}