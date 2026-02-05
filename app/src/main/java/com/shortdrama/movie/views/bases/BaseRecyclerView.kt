package com.shortdrama.movie.views.bases

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerView<VB : ViewDataBinding, T> :
    RecyclerView.Adapter<BaseRecyclerView<VB, T>.ViewHolder>() {

    abstract fun inflateBinding(inflater: LayoutInflater): VB
    abstract fun submitData(newData: List<T>)
    abstract fun setData(binding: VB, item: T, layoutPosition: Int)
    open fun onClickViews(binding: VB, obj: T, layoutPosition: Int) {}

    private var _binding: VB? = null
    protected val mBinding: VB get() = _binding!!
    val list: MutableList<T> = mutableListOf()
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        _binding = inflateBinding(LayoutInflater.from(parent.context))
        return ViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    override fun getItemCount(): Int {
        if (list.isNotEmpty()) {
            return list.size
        }
        return 0
    }


    inner class ViewHolder(var binding: VB) : BaseViewHolder<T>(binding) {

        override fun bindData(obj: T) {
            onClickViews(obj)
            setData(binding, obj, layoutPosition)
        }

        override fun onClickViews(obj: T) {
            onClickViews(binding, obj, layoutPosition)
        }
    }

}