package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.annotation.SuppressLint
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
import com.shortdrama.movie.databinding.ItemHomeTredingBannerBinding
import com.shortdrama.movie.views.bases.BaseViewHolder

class HomeTrendingBannerAdapter(
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : RecyclerView.Adapter<HomeTrendingBannerAdapter.DataViewHolder>() {
    private var mContext: Context? = null
    private val listItems = arrayListOf<DramaWithGenresUIModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(listPhotoSlide: List<DramaWithGenresUIModel>) {
        listItems.clear()
        listItems.addAll(listPhotoSlide)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        mContext = parent.context
        val binding: ItemHomeTredingBannerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.item_home_treding_banner,
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

    inner class DataViewHolder(var binding: ItemHomeTredingBannerBinding) :
        BaseViewHolder<DramaWithGenresUIModel>(binding) {
        override fun bindData(obj: DramaWithGenresUIModel) {
            val path = "${obj.dramaUIModel.dramaName}/${obj.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->Glide.with(binding.imgPlay.context).load(uri).into(binding.ivBanner) },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvNameVideo.text = obj.dramaUIModel.dramaName
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}