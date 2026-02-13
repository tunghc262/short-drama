package com.shortdrama.movie.views.activities.search.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemSearchBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView

class SearchAdapter(
    val activity: Activity,
    val onClickItem: (DramaWithGenresUIModel) -> Unit,
) : BaseRecyclerView<DramaWithGenresUIModel>() {

    private val displayList = mutableListOf<DramaWithGenresUIModel>()

    override fun getItemLayout(): Int = R.layout.item_search

    override fun submitData(newData: List<DramaWithGenresUIModel>) {
        list.clear()
        list.addAll(newData)
        displayList.clear()
        displayList.addAll(newData)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(query: String) {
        displayList.clear()

        if (query.isBlank()) {
            displayList.addAll(list)
        } else {
            val lowerQuery = query.lowercase()
            displayList.addAll(
                list.filter {
                    it.dramaUIModel.dramaName.lowercase().contains(lowerQuery)
                }
            )
        }
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ViewDataBinding,
        item: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        if (binding is ItemSearchBinding) {
            val path = "${item.dramaUIModel.dramaName}/${item.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            binding.tvDescription.text = item.dramaUIModel.dramaDescription
            item.dramaGenresUIModel.let { list ->
                if (list.isNotEmpty()) {
                    if (list.size > 2) {
                        binding.tvGenre2.text = list[1].genresName
                        binding.tvGenre3.text = list[2].genresName
                    } else if (list.size > 1) {
                        binding.tvGenre2.text = list[1].genresName
                    } else {
                        binding.tvGenre1.text = list[0].genresName
                    }
                }
            }
            binding.horizontalScroll.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemSearchBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}