package com.shortdrama.movie.views.activities.play_movie.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.PlayMovieSpeedModel
import com.shortdrama.movie.databinding.ItemPlaySpeedMovieBinding
import com.shortdrama.movie.views.bases.ext.setTextColorById

class PlayMovieSpeedAdapter(
    private val activity: Activity,
    val onClickItem: (PlayMovieSpeedModel) -> Unit
) :
    RecyclerView.Adapter<PlayMovieSpeedAdapter.ItemViewHolder>() {
    private var currentSelected = 2
    private val listData = mutableListOf<PlayMovieSpeedModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(newData: List<PlayMovieSpeedModel>, currentSelect: Int) {
        listData.clear()
        listData.addAll(newData)
        currentSelected = currentSelect
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ItemPlaySpeedMovieBinding.inflate(
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

    inner class ItemViewHolder(private val binding: ItemPlaySpeedMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(obj: PlayMovieSpeedModel) {
            binding.tvSpeed.text = obj.nameSpeed
            if (layoutPosition == currentSelected) {
                binding.clRoot.setBackgroundResource(R.drawable.bg_btn_fill_24)
                binding.tvSpeed.setTextColorById(R.color.white)
                binding.tvSpeed.typeface =
                    ResourcesCompat.getFont(activity, R.font.poppins_semibold)
            } else {
                binding.clRoot.setBackgroundResource(R.drawable.bg_btn_fill_24)
                binding.clRoot.backgroundTintList =
                    ContextCompat.getColorStateList(activity, R.color.color_2A2A2A)
                binding.tvSpeed.setTextColorById(R.color.white_60)
                binding.tvSpeed.typeface =
                    ResourcesCompat.getFont(activity, R.font.poppins_regular)
            }
            binding.clRoot.setOnClickListener {
                setSelectedItem(layoutPosition)
                onClickItem(obj)
            }
        }
    }

    fun setSelectedItem(position: Int) {
        notifyItemChanged(currentSelected)
        notifyItemChanged(position)
        currentSelected = position
    }
}