package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.LayoutItemTabHomeBinding
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.visibleView

class TabHomeAdapter(private val activity: Activity, val onClickItem: (Int) -> Unit) :
    RecyclerView.Adapter<TabHomeAdapter.ItemViewHolder>() {
    private var currentSelected = 0
    private val listData = mutableListOf<String>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newData: List<String>) {
        listData.clear()
        listData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = LayoutItemTabHomeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = listData[position]
        holder.bindData(item)
    }

    override fun getItemCount(): Int {
        if (listData.isNotEmpty()) {
            return listData.size
        }
        return 0
    }

    inner class ItemViewHolder(private val binding: LayoutItemTabHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(title: String) {
            binding.tvTitle.text = title
            if (currentSelected == layoutPosition) {
                binding.tvTitle.text = title
                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.white
                    )
                )
                val typeface = ResourcesCompat.getFont(activity, R.font.poppins_semibold)
                binding.tvTitle.typeface = typeface
                binding.ivLineBottom.visibleView()
            } else {
                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.white_40
                    )
                )
                val typeface = ResourcesCompat.getFont(activity, R.font.poppins_regular)
                binding.tvTitle.typeface = typeface
                binding.ivLineBottom.goneView()
            }
            binding.root.setOnClickListener {
                if (layoutPosition == currentSelected) return@setOnClickListener
                onClickItem(layoutPosition)
                setSelectedItem(layoutPosition)
            }
        }
    }

    fun setSelectedItem(position: Int) {
        notifyItemChanged(currentSelected)
        notifyItemChanged(position)
        currentSelected = position
    }
}