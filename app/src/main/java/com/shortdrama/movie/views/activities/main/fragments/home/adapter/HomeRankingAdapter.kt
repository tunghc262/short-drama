package com.shortdrama.movie.views.activities.main.fragments.home.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.module.ads.admob.natives.NativeInAppAll
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.core_api_storage.model_ui.DramaWithGenresUIModel
import com.module.core_api_storage.storage.StorageSource
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ItemAdsRankingBinding
import com.shortdrama.movie.databinding.ItemMovieRankingBinding
import com.shortdrama.movie.views.activities.see_more.popular.TYPE_ADS
import com.shortdrama.movie.views.activities.see_more.popular.TYPE_NORMAL
import com.shortdrama.movie.views.bases.BaseViewHolder
import com.shortdrama.movie.views.bases.ext.setTextColorById
import com.shortdrama.movie.views.bases.ext.visibleView

class HomeRankingAdapter(
    val activity: Activity,
    val isChangeWidth: Boolean = true,
    val onClickItem: (DramaWithGenresUIModel) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal var list = arrayListOf<DramaWithGenresUIModel>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ADS -> {
                val view: ItemAdsRankingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_ads_ranking,
                    parent,
                    false
                )
                ItemAdsViewHolder(view)
            }

            else -> {
                val view: ItemMovieRankingBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_movie_ranking,
                    parent,
                    false
                )
                MovieViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].isAds) {
            TYPE_ADS
        } else {
            TYPE_NORMAL
        }
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        val item = list[position]
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
        return list.size
    }

    fun submitData(newData: List<DramaWithGenresUIModel>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(private var binding: ItemMovieRankingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: DramaWithGenresUIModel) {
            onClickView(item)
            if (isChangeWidth) {
                val displayMetrics = binding.root.context.resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val params = binding.root.layoutParams
                params.width = (screenWidth * 0.8f).toInt()
                binding.root.layoutParams = params
            }
            val pos = layoutPosition + 1
            binding.tvNumberChart.text = pos.toString()
            when (pos) {
                1 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.red)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                2 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.purple)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                3 -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.blue)
                    binding.tvNumberChart.setTextColorById(R.color.white)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.white
                        )
                    )
                }

                else -> {
                    binding.llRankChart.backgroundTintList =
                        ContextCompat.getColorStateList(activity, R.color.white)
                    binding.tvNumberChart.setTextColorById(R.color.black)
                    binding.ivRankChart.setColorFilter(
                        ContextCompat.getColor(
                            activity,
                            R.color.black
                        )
                    )
                }
            }
            val path = "${item.dramaUIModel.dramaName}/${item.dramaUIModel.dramaThumb}"
            StorageSource.getStorageDownloadUrl(
                path,
                onSuccess = { uri ->
                    if (activity.isFinishing || activity.isDestroyed) return@getStorageDownloadUrl
                    Glide.with(activity).load(uri).into(binding.ivBannerMovie)
                },
                onError = {
                    Log.e("TAG", "bindData: ")
                })
            binding.tvMovieName.text = item.dramaUIModel.dramaName
            binding.tvDescription.text = item.dramaUIModel.dramaDescription
            item.dramaGenresUIModel.let { list ->
                if (list.isNotEmpty()) {
                    if (list.size > 2) {
                        binding.tvGenre2.text = list[1].genresName
                        binding.tvGenre3.text = list[2].genresName
                    } else if (list.size > 1) {
                        binding.tvGenre2.text = list[1].genresName
                    } else {
                        binding.tvGenre1.text = list[0].genresName
                    }
                }
            }
            binding.horizontalScroll.setOnTouchListener { v, event ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        private fun onClickView(item: DramaWithGenresUIModel) {
            binding.root.setOnClickListener {
                onClickItem(item)
            }
        }
    }

    inner class ItemAdsViewHolder(val mBinding: ItemAdsRankingBinding) :
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
                2,
            )
            mBinding.lnNative.visibleView()
        }
    }

    fun removeAdsIfFailed() {
        if (list.isNotEmpty() && list[2].isAds) {
            list.removeAt(2)
            notifyItemRemoved(2)
        }
    }
}