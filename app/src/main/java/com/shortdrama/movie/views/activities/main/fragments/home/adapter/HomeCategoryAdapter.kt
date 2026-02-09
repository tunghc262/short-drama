package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemHomeTredingBannerBinding
import com.shortdrama.movie.databinding.ItemMovieCategoryBinding
import com.shortdrama.movie.views.bases.BaseViewHolder

class HomeCategoryAdapter(
    val onClickItem: (TVSeriesUiModel) -> Unit
) : RecyclerView.Adapter<HomeCategoryAdapter.DataViewHolder>() {
    private var mContext: Context? = null
    private val listItems = arrayListOf<TVSeriesUiModel>()

    fun submitData(listPhotoSlide: List<TVSeriesUiModel>) {
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
        BaseViewHolder<TVSeriesUiModel>(binding) {
        override fun bindData(obj: TVSeriesUiModel) {
            Glide.with(binding.ivBannerMovie.context).load(obj.posterPath).into(binding.ivBannerMovie)
            binding.tvMovieName.text = obj.originalName
            binding.tvMovieStyle.text = obj.genres?.get(0)?.name
            binding.root.setOnClickListener {
                onClickItem(obj)
            }
        }
    }
}