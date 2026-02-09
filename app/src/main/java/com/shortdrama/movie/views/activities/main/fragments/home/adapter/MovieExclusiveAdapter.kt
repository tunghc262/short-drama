package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieExclusiveBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class MovieExclusiveAdapter(
    val onClickItem: (TVSeriesUiModel) -> Unit
) : BaseRecyclerView<TVSeriesUiModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_exclusive

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
        if (binding is ItemMovieExclusiveBinding) {
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = item.genres?.get(0)?.name
        }
    }

    override fun onClickViews(binding: ViewDataBinding, obj: TVSeriesUiModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieExclusiveBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}