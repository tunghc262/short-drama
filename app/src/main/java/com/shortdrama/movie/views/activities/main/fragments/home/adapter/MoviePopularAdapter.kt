package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.databinding.ItemMoviePopularBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.visibleView

class MoviePopularAdapter(
    val onClickItem: (TVSeriesUiModel) -> Unit
) : BaseRecyclerView<TVSeriesUiModel>() {
    override fun getItemLayout(): Int = com.shortdrama.movie.R.layout.item_movie_popular

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
        if (binding is ItemMoviePopularBinding) {
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = item.genres?.get(0)?.name
            if (layoutPosition in listOf(0, 1, 3)) {
                binding.tvHot.visibleView()
            }
        }
    }

    override fun onClickViews(binding: ViewDataBinding, obj: TVSeriesUiModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMoviePopularBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}