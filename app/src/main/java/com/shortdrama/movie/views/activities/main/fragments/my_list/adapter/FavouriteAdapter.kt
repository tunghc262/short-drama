package com.shortdrama.movie.views.activities.main.fragments.my_list.adapter

import android.app.Activity
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieFavoriteBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class FavouriteAdapter(
    val activity: Activity,
    val onClickItem: (DramaWithGenresUIModel) -> Unit,
    val onClickRemoveItem: (DramaWithGenresUIModel) -> Unit
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_favorite

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
        if (binding is ItemMovieFavoriteBinding) {
            val genreAdapter = GenreFavoriteAdapter()
            binding.rvGenre.apply {
                adapter = genreAdapter
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            }
            item.dramaGenresUIModel.let { genreAdapter.submitData(it) }
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
            binding.tvTitle.text = item.dramaUIModel.dramaName
            binding.tvDescription.text = item.dramaUIModel.dramaDescription
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
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