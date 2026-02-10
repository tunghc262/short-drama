package com.shortdrama.movie.views.activities.main.fragments.my_list.adapter

import android.app.Activity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieFavoriteBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class FavouriteAdapter(
    val activity: Activity,
    val onClickItem: (TVSeriesUiModel) -> Unit,
    val onClickRemoveItem: (TVSeriesUiModel) -> Unit
) : BaseRecyclerView<TVSeriesUiModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_favorite

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
        if (binding is ItemMovieFavoriteBinding) {
            val genreAdapter = GenreFavoriteAdapter()
            binding.rvGenre.apply {
                adapter = genreAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            }
            item.genres?.let { genreAdapter.submitData(it) }
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvTitle.text = item.name
            if (item.overview.isNullOrEmpty()) {
                binding.tvDescription.text =
                    "Discover a world of mystery and passion in this spectacular series."
            } else {
                binding.tvDescription.text = item.overview
            }
        }
    }

    override fun onClickViews(binding: ViewDataBinding, obj: TVSeriesUiModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieFavoriteBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
            binding.icMark.onClickAlpha {
                onClickRemoveItem(obj)
            }
        }
    }
}