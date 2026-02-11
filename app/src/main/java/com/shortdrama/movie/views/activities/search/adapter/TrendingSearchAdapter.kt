package com.shortdrama.movie.views.activities.search.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMoviePopularBinding
import com.shortdrama.movie.databinding.ItemSearchTrendingBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.visibleView

class TrendingSearchAdapter(
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_search_trending

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
        if (binding is ItemSearchTrendingBinding) {
            StorageSource.getStorageDownloadUrl(
                item.dramaUIModel.dramaThumb,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG_drama_thumb", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            if (item.dramaGenresUIModel.isNotEmpty()) {
                binding.tvMovieStyle.text = item.dramaGenresUIModel[0].genresName
            }
            if (layoutPosition in listOf(0, 1, 3)) {
                binding.tvHot.visibleView()
            }
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemSearchTrendingBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}