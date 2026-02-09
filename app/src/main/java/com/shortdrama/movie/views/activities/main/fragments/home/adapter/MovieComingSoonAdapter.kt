package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieComingSoonBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.toDayMonthNumeric
import com.shortdrama.movie.views.bases.ext.toMonthDayOrdinal

class MovieComingSoonAdapter(
    val isShowDateOriginal: Boolean = true,
    val iShowDateNumeric: Boolean = false,
    val onClickItem: (TVSeriesUiModel) -> Unit
) : BaseRecyclerView<TVSeriesUiModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_coming_soon

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
        if (binding is ItemMovieComingSoonBinding) {
            binding.tvDateNumeric.text = item.dateComingSoon?.toDayMonthNumeric()
            binding.tvDateOriginal.text = item.dateComingSoon?.toMonthDayOrdinal()
            if (!isShowDateOriginal) {
                binding.llDateOriginal.goneView()
            }
            if (!iShowDateNumeric) {
                binding.tvDateNumeric.goneView()
            }
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = item.genres?.get(0)?.name
            Log.e("DATE_TEST", item.dateComingSoon?.toMonthDayOrdinal() ?: "null")
        }
    }

    override fun onClickViews(binding: ViewDataBinding, obj: TVSeriesUiModel, layoutPosition: Int) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieComingSoonBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}