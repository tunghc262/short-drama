package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core_api.model.core.GenreTVSeriesModel
import com.shortdrama.movie.databinding.ItemContainerGenreBinding

class GenreMovieAdapter() : RecyclerView.Adapter<GenreMovieAdapter.ItemViewHolder>() {
    private val listData = mutableListOf<GenreTVSeriesModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newData: List<GenreTVSeriesModel>) {
        listData.clear()
        listData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemContainerGenreBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemContainerGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(obj: GenreTVSeriesModel) {
            binding.tvTitle.text = obj.name
        }
    }
}