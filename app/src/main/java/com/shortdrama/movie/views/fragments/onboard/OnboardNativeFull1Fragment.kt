package com.shortdrama.movie.views.fragments.onboard

import com.bumptech.glide.Glide
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.natives.NativeOnboardFullscreen1
import com.module.ads.callback.CallbackNative
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.databinding.FragmentOnboardBinding
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.setTextById
import com.shortdrama.movie.views.bases.ext.visibleView


class OnboardNativeFull1Fragment : BaseFragment<FragmentOnboardBinding>() {

    private var isAdShown = false
    private var isFirstResume = true

    companion object {
        fun newInstance(): OnboardNativeFull1Fragment {
            val fragment = OnboardNativeFull1Fragment()
            return fragment
        }
    }

    override fun getLayoutFragment(): Int {
        return R.layout.fragment_onboard
    }

    override fun initViews() {
        super.initViews()
        Glide.with(this).load(R.drawable.img_on_board_1).into(mBinding.ivThumb)
        mBinding.tvTitle.setTextById(R.string.title_on_boarding_4)
        mBinding.tvContent.setTextById(R.string.content_on_boarding_4)
        mBinding.rlAdsFullscreen.visibleView()
        mBinding.lnNative.invisibleView()
        mBinding.rlButton.goneView()
        mBinding.lavHand.visibleView()

        logEvent("native_ob_1_scr_view")
    }

    override fun onResume() {
        super.onResume()
        // Bỏ qua lần onResume đầu tiên ngay sau onCreate

        activity?.let { act ->
            if (isFirstResume) {
                initAdmob()
                isFirstResume = false
                return
            }

            if (!ResumeAdsManager.shouldReloadAd) {
                // Load lại và hiển thị quảng cáo mới trong onResume
                isAdShown = false // Reset để cho phép hiển thị quảng cáo mới
                NativeOnboardFullscreen1.getInstance().destroyNativeAd()
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
            if ((NativeOnboardFullscreen1.nativeAdHigh != null || NativeOnboardFullscreen1.nativeAdNormal != null) && !isAdShown) {
                showAds()
            } else if (!isAdShown) {
                NativeOnboardFullscreen1.getInstance().setCallbackNative(object : CallbackNative {
                    override fun onLoaded() {
                        if ((NativeOnboardFullscreen1.nativeAdHigh != null || NativeOnboardFullscreen1.nativeAdNormal != null) && !isAdShown) {
                            showAds()
                        }
                    }

                    override fun onFailed() {}
                    override fun onAdImpression() {}
                })
                NativeOnboardFullscreen1.getInstance().loadAdsAll(act)
            }
        }
    }

    private fun showAds() {
        activity?.let { act ->
            mBinding.rlAdsFullscreen.visibleView()
            NativeOnboardFullscreen1.getInstance().showAdsAll(
                act,
                mBinding.lnNativeFullscreen,
                AdPlaceName.NATIVE_ONBOARD_FULLSCREEN_1.name.lowercase()
            )
            isAdShown = true
        }
    }

    override fun onDestroyView() {
        NativeOnboardFullscreen1.getInstance().destroyNativeAd() // Xóa quảng cáo cũ
        super.onDestroyView()
    }
}