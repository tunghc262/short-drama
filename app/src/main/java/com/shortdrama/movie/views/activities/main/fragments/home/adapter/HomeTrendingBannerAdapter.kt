package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemHomeTredingBannerBinding
import com.shortdrama.movie.views.bases.BaseViewHolder

class HomeTrendingBannerAdapter(
    private val activity: Activity,
    val onClickItem: (Int) -> Unit
) : RecyclerView.Adapter<HomeTrendingBannerAdapter.DataViewHolder>() {
    private var mContext: Context? = null
    private val listItems = arrayListOf<Int>()

    fun submitData(listPhotoSlide: List<Int>) {
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
        BaseViewHolder<Int>(binding) {
        override fun bindData(obj: Int) {
            mContext?.let {
                Glide.with(it).load(obj).into(binding.ivBanner)
                binding.root.setOnClickListener {
                    onClickItem(obj)
                }
            }
        }
    }

}