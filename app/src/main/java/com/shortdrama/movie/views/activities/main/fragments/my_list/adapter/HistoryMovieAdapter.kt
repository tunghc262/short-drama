package com.shortdrama.movie.views.activities.main.fragments.my_list.adapter

import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.shortdrama.movie.R
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.databinding.ItemMovieNewReleaseBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class HistoryMovieAdapter(
    val onClickItem: (HistoryWatchEntity) -> Unit
) : BaseRecyclerView<HistoryWatchEntity>() {
    override fun getItemLayout(): Int = R.layout.item_movie_new_release

    override fun submitData(newData: List<HistoryWatchEntity>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: HistoryWatchEntity,
        layoutPosition: Int
    ) {
        if (binding is ItemMovieNewReleaseBinding) {
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = item.genres?.get(0)?.name

        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: HistoryWatchEntity,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieNewReleaseBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}