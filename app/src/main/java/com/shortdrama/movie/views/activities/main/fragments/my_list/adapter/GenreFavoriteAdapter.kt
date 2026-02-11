package com.shortdrama.movie.views.activities.main.fragments.my_list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.module.core_api_storage.model_ui.DramaGenresUIModel
import com.shortdrama.movie.databinding.ItemGenreFavoriteBinding

class GenreFavoriteAdapter() : RecyclerView.Adapter<GenreFavoriteAdapter.ItemViewHolder>() {
    private val listData = mutableListOf<DramaGenresUIModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newData: List<DramaGenresUIModel>) {
        listData.clear()
        listData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemGenreFavoriteBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemGenreFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(obj: DramaGenresUIModel) {
            binding.tvTitle.text = obj.genresName
        }
    }
}