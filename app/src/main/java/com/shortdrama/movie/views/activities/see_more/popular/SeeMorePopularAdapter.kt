package com.shortdrama.movie.views.activities.see_more.popular

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.module.ads.admob.natives.NativeInAppAll
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
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
    private val onClickItem: (DramaWithGenresUIModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal var listMovie = arrayListOf<DramaWithGenresUIModel>()

    fun submitData(newData: List<DramaWithGenresUIModel>) {
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
        BaseViewHolder<DramaWithGenresUIModel>(mBinding) {
        override fun bindData(obj: DramaWithGenresUIModel) {
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
                1,
            )
            mBinding.lnNative.visibleView()
        }
    }

    inner class MovieViewHolder(private var binding: ItemMoviePopularBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: DramaWithGenresUIModel) {
            onClickView(item)
            StorageSource.getStorageDownloadUrl(
                item.dramaUIModel.dramaThumb,
                onSuccess = { uri ->
                    Glide.with(binding.ivBannerMovie.context).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG_drama_thumb", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            if (item.dramaGenresUIModel.isNotEmpty()) {
                binding.tvMovieStyle.text = item.dramaGenresUIModel[0].genresName
            }
            val listHot = if (listMovie[1].isAds) listOf(0, 2, 3) else listOf(0, 1, 3)
            if (layoutPosition in listHot) {
                binding.tvHot.visibleView()
            }
        }

        private fun onClickView(item: DramaWithGenresUIModel) {
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