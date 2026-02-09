package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemTabCategoryBinding

class TabCategoryAdapter(
    private val activity: Activity,
    val isFromDialog: Boolean = false,
    val onClickItem: (Int) -> Unit
) :
    RecyclerView.Adapter<TabCategoryAdapter.ItemViewHolder>() {
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
        val binding = ItemTabCategoryBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemTabCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(title: String) {
            binding.tvTitle.text = title
            if (currentSelected == layoutPosition) {
                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.white
                    )
                )
                binding.root.setBackgroundResource(R.drawable.bg_button_cate_1)
            } else {
                binding.tvTitle.setTextColor(
                    ContextCompat.getColor(
                        activity,
                        R.color.color_787B83
                    )
                )
                if (isFromDialog) {
                    binding.root.setBackgroundResource(R.drawable.bg_button_cate_3)
                } else {
                    binding.root.setBackgroundResource(R.drawable.bg_button_cate_2)
                }

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