package com.shortdrama.movie.views.activities.language

import android.view.LayoutInflater
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.LanguageModel
import com.shortdrama.movie.databinding.ItemLanguageBinding
import com.shortdrama.movie.views.bases.BaseRecyclerView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.setTextColorById
import com.shortdrama.movie.views.bases.ext.visibleView

class LanguageAdapter(
    val isLanguageDup: Boolean = false,
    val isLanguageSetting: Boolean = false,
    val onClickItemLanguage: (LanguageModel, Int) -> Unit
) : BaseRecyclerView<ItemLanguageBinding, LanguageModel>() {

    private var currentSelected = -1

    override fun inflateBinding(inflater: LayoutInflater): ItemLanguageBinding =
        ItemLanguageBinding.inflate(inflater)

    override fun submitData(newData: List<LanguageModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    override fun setData(
        binding: ItemLanguageBinding,
        item: LanguageModel,
        layoutPosition: Int
    ) {
        context?.let { ctx ->
            binding.tvLanguage.text = item.languageName
            item.isCheck = currentSelected == layoutPosition
            if (item.isCheck) {
                binding.tvLanguage.setTextColorById(R.color.black)
                binding.rlLanguage.setBackgroundResource(R.drawable.bg_item_language_selected)
                binding.ivCheck.setImageResource(R.drawable.ic_checkbox_selected)
            } else {
                binding.tvLanguage.setTextColorById(R.color.black)
                binding.rlLanguage.setBackgroundResource(R.drawable.bg_item_language)
                binding.ivCheck.setImageResource(R.drawable.ic_checkbox)
            }
        }

        if (isLanguageSetting) {
            binding.lavHand.invisibleView()
        } else {
            if (isLanguageDup) {
                binding.lavHand.invisibleView()
            } else {
                if (layoutPosition == 2) {
                    binding.lavHand.visibleView()
                } else {
                    binding.lavHand.invisibleView()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            return list.size
        }
        return 0
    }

    override fun onClickViews(
        binding: ItemLanguageBinding,
        obj: LanguageModel,
        layoutPosition: Int
    ) {
        super.onClickViews(binding, obj, layoutPosition)
        binding.clRoot.setOnClickListener {
            if (currentSelected != layoutPosition) {
                setSelectedItem(layoutPosition)
                onClickItemLanguage(obj, layoutPosition)

            }
        }
    }

    fun setSelectedItem(position: Int) {
        notifyItemChanged(currentSelected)
        notifyItemChanged(position)
        currentSelected = position
    }
}