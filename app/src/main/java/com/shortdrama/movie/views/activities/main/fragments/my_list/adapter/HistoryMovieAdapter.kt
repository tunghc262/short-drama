package com.shortdrama.movie.views.activities.main.fragments.my_list.adapter

import android.app.Activity
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.module.core_api_storage.model_ui.DramaGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.data.entity.HistoryWatchEntity
import com.shortdrama.movie.databinding.ItemMovieNewReleaseBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class HistoryMovieAdapter(
    val activity: Activity,
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
            val path = "${item.name}/${item.thumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    if (activity.isFinishing || activity.isDestroyed) return@getStorageDownloadUrl
                    Glide.with(activity).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG_drama_thumb", "bindData: ")
                })
            val list = Gson().fromJson<List<DramaGenresUIModel>>(
                item.genresJson,
                object : TypeToken<List<DramaGenresUIModel>>() {}.type
            ) ?: emptyList()
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = list[0].genresName
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