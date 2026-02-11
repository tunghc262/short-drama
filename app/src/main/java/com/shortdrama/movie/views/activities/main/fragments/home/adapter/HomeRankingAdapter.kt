package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.app.Activity
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieRankingBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.setTextColorById

class HomeRankingAdapter(
    val activity: Activity,
    val isChangeWidth: Boolean = true,
    val onClickItem: (DramaWithGenresUIModel) -> Unit,
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_ranking

    override fun submitData(newData: List<DramaWithGenresUIModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        if (binding is ItemMovieRankingBinding) {
            if (isChangeWidth){
                val displayMetrics = binding.root.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val params = binding.root.layoutParams
                params.width = (screenWidth * 0.8f).toInt()
                binding.root.layoutParams = params
            }
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
            StorageSource.getStorageDownloadUrl(
                item.dramaUIModel.dramaThumb,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            binding.tvDescription.text = item.dramaUIModel.dramaDescription
            item.dramaGenresUIModel.let { list ->
                if (list.isNotEmpty()) {
                    if (list.size > 2) {
                        binding.tvGenre2.text = list[1].genresName
                        binding.tvGenre3.text = list[2].genresName
                    } else if (list.size > 1) {
                        binding.tvGenre2.text = list[1].genresName
                    } else {
                        binding.tvGenre1.text = list[0].genresName
                    }
                }
            }
            binding.horizontalScroll.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieRankingBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}