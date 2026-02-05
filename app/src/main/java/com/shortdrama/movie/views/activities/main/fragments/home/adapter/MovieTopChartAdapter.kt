package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.app.Activity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieTopChartBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.setTextColorById

class MovieTopChartAdapter(
    val activity: Activity,
    val onClickItem: (TVSeriesUiModel) -> Unit
) : BaseRecyclerView<TVSeriesUiModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_top_chart

    override fun submitData(newData: List<TVSeriesUiModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: TVSeriesUiModel,
        layoutPosition: Int
    ) {
        if (binding is ItemMovieTopChartBinding) {
            val displayMetrics = binding.root.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels

            val params = binding.root.layoutParams
            params.width = (screenWidth * 0.8f).toInt()
            binding.root.layoutParams = params
            val pos = layoutPosition + 1
            binding.tvNumberChart.text = pos.toString()
            when (pos) {
                1 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.red)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                2 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.purple)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                3 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.blue)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                else -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.white)
                    binding.tvNumberChart.setTextColorById(R.color.black)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.black
                        )
                    )
                }
            }
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            if (item.overview.isNullOrEmpty()) {
                binding.tvDescription.text =
                    "Discover a world of mystery and passion in this spectacular series."
            } else {
                binding.tvDescription.text = item.overview
            }
            item.genres?.let { list ->
                if (list.isNotEmpty()) {
                    if (list.size > 2) {
                        binding.tvGenre2.text = list[1].name
                        binding.tvGenre3.text = list[2].name
                    } else if (list.size > 1) {
                        binding.tvGenre2.text = list[1].name
                    } else {
                        binding.tvGenre1.text = list[0].name
                    }
                }
            }
            binding.horizontalScroll.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }
    }

    override fun onClickViews(binding: ViewDataBinding, obj: TVSeriesUiModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieTopChartBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}