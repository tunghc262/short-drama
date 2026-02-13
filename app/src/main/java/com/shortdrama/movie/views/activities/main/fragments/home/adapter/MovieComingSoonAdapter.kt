package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemMovieComingSoonBinding
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.toDayMonthNumeric
import com.shortdrama.movie.views.bases.ext.toMonthDayOrdinal
import com.shortdrama.movie.views.bases.ext.visibleView

class MovieComingSoonAdapter(
    val isShowDateOriginal: Boolean = true,
    val iShowDateNumeric: Boolean = false,
    val onClickItem: (DramaWithGenresUIModel) -> Unit
) : BaseRecyclerView<DramaWithGenresUIModel>() {
    override fun getItemLayout(): Int = R.layout.item_movie_coming_soon

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
        if (binding is ItemMovieComingSoonBinding) {
            val key = "coming_soon_${item.dramaUIModel.dramaId}"
            if (SharePrefUtils.getBoolean(key, false)) {
                binding.ivRemind.setImageResource(R.drawable.ic_reminded)
                binding.tvRemind.text = "Reminded"
            }else{
                binding.ivRemind.setImageResource(R.drawable.ic_remind)
                binding.tvRemind.text = "Remind"
            }
            binding.tvDateNumeric.text = item.dateComingSoon.toDayMonthNumeric()
            binding.tvDateOriginal.text = item.dateComingSoon.toMonthDayOrdinal()
            if (!isShowDateOriginal) {
                binding.llDateOriginal.goneView()
            } else {
                binding.llDateOriginal.visibleView()
            }
            if (!iShowDateNumeric) {
                binding.tvDateNumeric.goneView()
            } else {
                binding.tvDateNumeric.visibleView()
            }
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
            if (item.dramaGenresUIModel.isNotEmpty()) {
                binding.tvMovieStyle.text = item.dramaGenresUIModel[0].genresName
            }
            Log.e("DATE_TEST", item.dateComingSoon.toMonthDayOrdinal())
        }
    }

    override fun onClickViews(
        binding: ViewDataBinding,
        obj: DramaWithGenresUIModel,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        if (binding is ItemMovieComingSoonBinding) {
            binding.root.setOnClickListener {
                onClickItem(obj)
                val key = "coming_soon_${obj.dramaUIModel.dramaId}"
                SharePrefUtils.putBoolean(key, true)
                notifyItemChanged(layoutPosition)
            }
        }
    }
}