package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieCategoryBinding
import com.shortdrama.movie.views.bases.BaseViewHolder

class HomeCategoryAdapter(
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : RecyclerView.Adapter<HomeCategoryAdapter.DataViewHolder>() {
    private var mContext: Context? = null
    private val listItems = arrayListOf<DramaWithGenresUIModel>()

    fun submitData(listPhotoSlide: List<DramaWithGenresUIModel>) {
        listItems.clear()
        listItems.addAll(listPhotoSlide)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        mContext = parent.context
        val binding: ItemMovieCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_movie_category,
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = listItems[position]
        holder.bindData(item)
    }

    override fun getItemCount(): Int {
        if (listItems.isNotEmpty()) {
            return listItems.size
        }
        return 0
    }

    inner class DataViewHolder(var binding: ItemMovieCategoryBinding) :
        BaseViewHolder<DramaWithGenresUIModel>(binding) {
        override fun bindData(obj: DramaWithGenresUIModel) {
            val path = "${obj.dramaUIModel.dramaName}/${obj.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvMovieName.text = obj.dramaUIModel.dramaName
            if (obj.dramaGenresUIModel.isNotEmpty()) {
                binding.tvMovieStyle.text = obj.dramaGenresUIModel[0].genresName
            }
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}