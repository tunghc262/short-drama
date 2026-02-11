package com.shortdrama.movie.views.activities.see_more.popular

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.core_api.model.ui.TVSeriesUiModel
import com.module.ads.admob.natives.NativeInAppAll
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.databinding.ItemAdsPopularBinding
import com.shortdrama.movie.databinding.ItemMoviePopularBinding
import com.shortdrama.movie.views.bases.BaseViewHolder
import com.shortdrama.movie.views.bases.ext.visibleView


const val TYPE_NORMAL = 1
const val TYPE_ADS = 2

class SeeMorePopularAdapter(
    val activity: Activity,
    private val onClickItem: (TVSeriesUiModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var listMovie = arrayListOf<TVSeriesUiModel>()

    fun submitData(newData: List<TVSeriesUiModel>) {
        listMovie.clear()
        listMovie.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADS -> {
                val view: ItemAdsPopularBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_ads_popular,
                    parent,
                    false
                )
                ItemAdsViewHolder(view)
            }

            else -> {
                val view: ItemMoviePopularBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_movie_popular,
                    parent,
                    false
                )
                MovieViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listMovie[position].isAds) {
            TYPE_ADS
        } else {
            TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = listMovie[position]
        when (holder.itemViewType) {
            TYPE_NORMAL -> {
                (holder as MovieViewHolder).bindData(item)
            }

            TYPE_ADS -> {
                (holder as ItemAdsViewHolder).bindData(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    inner class ItemAdsViewHolder(val mBinding: ItemAdsPopularBinding) :
        BaseViewHolder<TVSeriesUiModel>(mBinding) {
        override fun bindData(obj: TVSeriesUiModel) {
            initAds()
        }

        fun initAds() {
            NativeInAppAll.getInstance().loadAndShow(
                activity,
                mBinding.lnNative,
                FirebaseQuery.getIdNativeInApp(),
                object : CallbackNative {
                    override fun onLoaded() {
                        mBinding.lnNative.visibleView()
                        Log.e("TAG", "onLoaded all: TemplateAdapter item")
                    }

                    override fun onFailed() {
                        Log.e("TAG", "onFailed all: TemplateAdapter item")
                        removeAdsIfFailed()
                    }

                    override fun onAdImpression() {

                    }
                },
                AdPlaceName.NATIVE_POPULAR.name.lowercase(),
            )
            mBinding.lnNative.visibleView()
        }
    }

    inner class MovieViewHolder(private var binding: ItemMoviePopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: TVSeriesUiModel) {
            onClickView(item)
            Glide.with(binding.ivBannerMovie.context).load(item.posterPath)
                .into(binding.ivBannerMovie)
            binding.tvMovieName.text = item.name
            binding.tvMovieStyle.text = item.genres?.get(0)?.name
            if (layoutPosition in listOf(0, 1, 3)) {
                binding.tvHot.visibleView()
            }
        }

        private fun onClickView(item: TVSeriesUiModel) {
            binding.root.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    fun removeAdsIfFailed() {
        if (listMovie.isNotEmpty() && listMovie[1].isAds) {
            listMovie.removeAt(1)
            notifyItemRemoved(1)
        }
    }
}