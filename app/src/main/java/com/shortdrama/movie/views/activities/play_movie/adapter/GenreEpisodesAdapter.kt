package com.shortdrama.movie.views.activities.play_movie.adapter

import androidx.databinding.ViewDataBinding
import com.example.core_api.model.core.GenreTVSeriesModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemGenreEpisodesBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class GenreEpisodesAdapter() : BaseRecyclerView<GenreTVSeriesModel>() {
    override fun getItemLayout(): Int = R.layout.item_genre_episodes

    override fun submitData(newData: List<GenreTVSeriesModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: GenreTVSeriesModel,
        layoutPosition: Int
    ) {
        if (binding is ItemGenreEpisodesBinding) {
            binding.tvTitle.text = item.name
        }
    }
}