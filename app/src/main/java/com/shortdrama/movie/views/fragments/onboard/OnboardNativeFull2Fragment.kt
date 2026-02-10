package com.shortdrama.movie.views.fragments.onboard

import com.bumptech.glide.Glide
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.natives.NativeOnboardFullscreen2
import com.module.ads.callback.CallbackNative
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.databinding.FragmentOnboardBinding
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.setTextById
import com.shortdrama.movie.views.bases.ext.visibleView


class OnboardNativeFull2Fragment : BaseFragment<FragmentOnboardBinding>() {

    private var isAdShown = false
    private var isFirstResume = true

    companion object {
        fun newInstance(): OnboardNativeFull2Fragment {
            val fragment = OnboardNativeFull2Fragment()
            return fragment
        }
    }

    override fun getLayoutFragment(): Int {
        return R.layout.fragment_onboard
    }

    override fun initViews() {
        super.initViews()
        Glide.with(this).load(R.drawable.img_onboard_1).into(mBinding.ivThumb)
        mBinding.tvTitle.setTextById(R.string.title_on_boarding_4)
        mBinding.tvContent.setTextById(R.string.content_on_boarding_4)
        mBinding.rlAdsFullscreen.visibleView()
        mBinding.lnNative.invisibleView()
        mBinding.rlButton.goneView()
        mBinding.lavHand.visibleView()

        logEvent("native_ob_2_scr_view")
    }

    override fun onResume() {
        super.onResume()
        activity?.let { act ->
            if (isFirstResume) {
                initAdmob()
                isFirstResume = false
                return
            }

            if (!ResumeAdsManager.shouldReloadAd) {
                // Load lại và hiển thị quảng cáo mới trong onResume
                isAdShown = false // Reset để cho phép hiển thị quảng cáo mới
                NativeOnboardFullscreen2.getInstance().destroyNativeAd()
                showOrLoadNativeAd() // Load và hiển thị quảng cáo mới
            } else {
                ResumeAdsManager.shouldReloadAd = false
            }
        }
    }

    private fun initAdmob() {
        showOrLoadNativeAd()
    }

    private fun showOrLoadNativeAd() {
        activity?.let { act ->
            if ((NativeOnboardFullscreen2.nativeAdHigh != null || NativeOnboardFullscreen2.nativeAdNormal != null) && !isAdShown) {
                showAds()
            } else if (!isAdShown) {
                NativeOnboardFullscreen2.getInstance().setCallbackNative(object : CallbackNative {
                    override fun onLoaded() {
                        if ((NativeOnboardFullscreen2.nativeAdHigh != null || NativeOnboardFullscreen2.nativeAdNormal != null) && !isAdShown) {
                            showAds()
                        }
                    }

                    override fun onFailed() {}
                    override fun onAdImpression() {}
                })
                NativeOnboardFullscreen2.getInstance().loadAdsAll(act)
            }
        }
    }

    private fun showAds() {
        activity?.let { act ->
            mBinding.rlAdsFullscreen.visibleView()
            NativeOnboardFullscreen2.getInstance().showAdsAll(
                act,
                mBinding.lnNativeFullscreen,
                AdPlaceName.NATIVE_ONBOARD_FULLSCREEN_2.name.lowercase()
            )
            isAdShown = true
        }
    }

    override fun onDestroyView() {
        NativeOnboardFullscreen2.getInstance().destroyNativeAd() // Xóa quảng cáo cũ
        super.onDestroyView()
    }
}