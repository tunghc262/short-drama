package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.app.Activity
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.databinding.ItemMoviePopularBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.visibleView

class MoviePopularAdapter(
    val activity: Activity,
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = com.shortdrama.movie.R.layout.item_movie_popular

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
        if (binding is ItemMoviePopularBinding) {
            val path = "${item.dramaUIModel.dramaName}/${item.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    if (activity.isFinishing || activity.isDestroyed) return@getStorageDownloadUrl
                    Glide.with(activity).load(uri).into(binding.ivBannerMovie)
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
        if (binding is ItemMoviePopularBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}