package com.shortdrama.movie.views.activities.play_movie.adapter

import androidx.databinding.ViewDataBinding
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.ResolutionMovieModel
import com.shortdrama.movie.databinding.ItemResolutionMovieBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView

class ResolutionMovieAdapter(
    val onClickItem: (ResolutionMovieModel) -> Unit
) : BaseRecyclerView<ResolutionMovieModel>() {
    private var currentSelected = 0

    override fun getItemLayout(): Int = R.layout.item_resolution_movie

    fun submitListData(newData: List<ResolutionMovieModel>, newSelected: Int) {
        currentSelected = newSelected
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun submitData(newData: List<ResolutionMovieModel>) {}

    override fun setData(
        binding: ViewDataBinding,
        item: ResolutionMovieModel,
        layoutPosition: Int
    ) {
        if (binding is ItemResolutionMovieBinding) {
            if (layoutPosition == 1 || layoutPosition == 2) {
                binding.tvPro.visibleView()
            } else {
                binding.tvPro.goneView()
            }
            binding.tvTitle.text = item.title
            binding.tvDes.text = item.description
            if (layoutPosition == currentSelected) {
                binding.clResolution.setBackgroundResource(R.drawable.bg_item_language_selected)
                binding.ivCheckBox.setImageResource(R.drawable.ic_checkbox_selected_2)
            } else {
                binding.clResolution.setBackgroundResource(R.drawable.bg_btn_2a2a2a)
                binding.ivCheckBox.setImageResource(R.drawable.ic_checkbox)
            }
            binding.clResolution.setOnClickListener {
                setSelectedItem(layoutPosition)
                onClickItem(item)
            }
        }
    }

    fun setSelectedItem(position: Int) {
        notifyItemChanged(currentSelected)
        notifyItemChanged(position)
        currentSelected = position
    }
}