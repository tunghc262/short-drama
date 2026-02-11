package com.shortdrama.movie.views.activities.recomend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.widget.CompositePageTransformer
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogRecommendMovieBinding
import kotlinx.coroutines.launch
import kotlin.math.abs

class RecommendDialog() : DialogFragment() {

    private var _binding: DialogRecommendMovieBinding? = null
    private val binding get() = _binding!!
    private var recommendMovieAdapter: RecommendAdapter? = null
    private var pos = 0
    private var listTVSeries: List<DramaWithGenresUIModel> = mutableListOf()
//    private val movieViewModel: MovieViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(STYLE_NORMAL, R.style.FullScreenDialogTheme)
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
        observerData()
    }

    private fun observerData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                movieViewModel.trending.collect { list ->
//                    if (list.isNotEmpty()) {
//                        recommendMovieAdapter?.submitData(list)
//                        listTVSeries = list
//                    }
//                }
            }
        }
    }

    private fun initView() {
        recommendMovieAdapter = RecommendAdapter(requireActivity())
//        binding.vpRecommendMovie.apply {
//            adapter = recommendMovieAdapter
//            offscreenPageLimit = 3
//        }
        setupViewPagerEffect()

//        binding.vpRecommendMovie.registerOnPageChangeCallback(object :
//            ViewPager2.OnPageChangeCallback() {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                binding.tvMovieName.text = listTVSeries[position].name
//                pos = position
//            }
//
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//
//            }
//        })
    }

    private fun setupViewPagerEffect() {
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer { page, position ->
            val absPosition = abs(position)

            val scale = if (absPosition <= 0.5f) {
                1.18f - (absPosition * 0.6f)
            } else {
                0.9f - (absPosition - 0.5f) * 0.2f
            }.coerceIn(0.9f, 1.18f)

            page.scaleX = scale
            page.scaleY = scale
            val translationY = if (absPosition <= 0.5f) {
                absPosition
            } else {
                absPosition
            }

            page.translationY = translationY
            page.alpha = if (absPosition <= 0.5f) {
                1f - (absPosition * 0.6f)
            } else {
                0.7f - (absPosition - 0.5f) * 0.4f
            }.coerceIn(0.5f, 1f)

            val translationX =
                -position * (page.width / 2f)
            page.translationX = translationX
        }
//        binding.vpRecommendMovie.setPageTransformer(compositePageTransformer)
    }

    private fun onClickView() {
//        binding.ivClose.onClickAlpha {
//            dismiss()
//        }
//        binding.llButtonWatchMovie.onClickAlpha {
//            val intent = Intent(activity, PlayMovieActivity::class.java)
//            intent.putExtra(
//                AppConstants.OBJ_MOVIE,
//                listTVSeries[pos]
//            )
//            startActivity(intent)
//            dismiss()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}