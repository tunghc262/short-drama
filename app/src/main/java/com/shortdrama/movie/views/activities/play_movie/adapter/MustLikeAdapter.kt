package com.shortdrama.movie.views.activities.play_movie.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieNewReleaseBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class MustLikeAdapter(
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_new_release

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
        if (binding is ItemMovieNewReleaseBinding) {
            StorageSource.getStorageDownloadUrl(
                item.dramaUIModel.dramaThumb,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            if (item.dramaGenresUIModel.isNotEmpty()) {
                binding.tvMovieStyle.text = item.dramaGenresUIModel[0].genresName
            }
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
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