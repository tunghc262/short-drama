package com.shortdrama.movie.views.activities.recomend

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.databinding.ItemRecommendMovieBinding

class RecommendAdapter(val activity: Activity) :
    RecyclerView.Adapter<RecommendAdapter.ItemViewHolder>() {

    private var listMovie = mutableListOf<DramaWithGenresUIModel>()
    fun submitData(newData: List<DramaWithGenresUIModel>) {
        listMovie.clear()
        listMovie.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRecommendMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listMovie[position], activity)
    }

    override fun getItemCount() = listMovie.size

    class ItemViewHolder(val binding: ItemRecommendMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(obj: DramaWithGenresUIModel, activity: Activity) {
            val path = "${obj.dramaUIModel.dramaName}/${obj.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    if (activity.isFinishing || activity.isDestroyed) return@getStorageDownloadUrl
                    Glide.with(activity).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG_drama_thumb", "bindData: ")
                })
        }
    }
}