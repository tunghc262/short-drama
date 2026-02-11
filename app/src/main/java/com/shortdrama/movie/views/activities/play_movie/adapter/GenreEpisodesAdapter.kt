package com.shortdrama.movie.views.activities.play_movie.adapter

import androidx.databinding.ViewDataBinding
import com.module.core_api_storage.model_ui.DramaGenresUIModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemGenreEpisodesBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class GenreEpisodesAdapter() : BaseRecyclerView<DramaGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_genre_episodes

    override fun submitData(newData: List<DramaGenresUIModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: DramaGenresUIModel,
        layoutPosition: Int
    ) {
        if (binding is ItemGenreEpisodesBinding) {
            binding.tvTitle.text = item.genresName
        }
    }
}