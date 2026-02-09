package com.shortdrama.movie.views.dialogs

import android.app.Activity
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.DialogCategoryTabBinding
import com.shortdrama.movie.views.activities.main.fragments.home.adapter.TabCategoryAdapter
import com.shortdrama.movie.views.bases.BaseBottomSheetDialog
import com.shortdrama.movie.views.bases.ext.onClickAlpha

class CategoryTabDialog(
    val activity: Activity,
    currentIndex: Int,
    val onClickItem: (Int) -> Unit
) : BaseBottomSheetDialog<DialogCategoryTabBinding>(activity) {
    private var indexTab = currentIndex
    private var tabCategoryAdapter: TabCategoryAdapter? = null
    override fun getLayoutDialog(): Int = R.layout.dialog_category_tab

    override fun initViews() {
        super.initViews()
        tabCategoryAdapter = TabCategoryAdapter(activity, true) { index ->
            indexTab = index
        }
        val listCate = listOf(
            "All",
            "Romantic",
            "Drama",
            "VIP",
            "Men",
            "18+",
            "School",
            "Porn"
        )
        mBinding.rvCateCategories.apply {
            adapter = tabCategoryAdapter
            layoutManager = FlexboxLayoutManager(activity).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
        }
        tabCategoryAdapter?.submitData(listCate)
        tabCategoryAdapter?.setSelectedItem(indexTab)
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.onClickAlpha {
            dismiss()
        }
        mBinding.btnConfirm.onClickAlpha {
            onClickItem(indexTab)
            dismiss()
        }
    }


}