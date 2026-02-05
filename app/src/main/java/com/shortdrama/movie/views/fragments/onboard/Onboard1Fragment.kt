package com.shortdrama.movie.views.fragments.onboard


import android.util.Log
import com.bumptech.glide.Glide
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.natives.NativeOnboard1
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.databinding.FragmentOnboardBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.activities.onboard.OnboardActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.onClick
import com.shortdrama.movie.views.bases.ext.setTextById


class Onboard1Fragment : BaseFragment<FragmentOnboardBinding>() {

    private var isAdShown = false
    private var isFirstResume = true

    companion object {
        fun newInstance(): Onboard1Fragment {
            val fragment = Onboard1Fragment()
            return fragment
        }
    }

    override fun getLayoutFragment(): Int {
        return R.layout.fragment_onboard
    }

    override fun initViews() {
        super.initViews()

        Glide.with(this).load(R.drawable.img_on_board_1).into(mBinding.ivThumb)
        mBinding.tvTitle.setTextById(R.string.title_on_boarding_1)
        mBinding.tvContent.setTextById(R.string.content_on_boarding_1)

        mBinding.rlAdsFullscreen.goneView()
        mBinding.lavHand.goneView()

        mBinding.ivIndicator1.setImageResource(R.drawable.circle_indicator_selected)
        mBinding.ivIndicator2.setImageResource(R.drawable.circle_indicator)
        mBinding.ivIndicator3.setImageResource(R.drawable.circle_indicator)
        mBinding.tvNext.setTextById(R.string.text_next)

        activity?.let { act ->
            if (AppUtils.isSession2()) {
                logEvent("onboarding_1_ss_view")
            } else {
                logEvent("onboarding_1_view")
            }
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvNext.onClick {
            activity?.let { act ->
                if (act is OnboardActivity) {
                    act.onNextPage()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Bỏ qua lần onResume đầu tiên ngay sau onCreate
        Log.e("TAG", "onResume: Onboard 1")
        activity?.let { act ->
            if (PurchaseUtils.isNoAds(act) || !FirebaseQuery.getEnableAds()) {
                mBinding.lnNative.goneView()
            }
        }

        if (isFirstResume) {
            initAdmob()
            isFirstResume = false
            return
        }

        if (!ResumeAdsManager.shouldReloadAd) {
            // Load lại và hiển thị quảng cáo mới trong onResume
            isAdShown = false // Reset để cho phép hiển thị quảng cáo mới
            NativeOnboard1.getInstance().destroyNativeAd() // Xóa quảng cáo cũ
            showOrLoadNativeAd() // Load và hiển thị quảng cáo mới
        } else {
            ResumeAdsManager.shouldReloadAd = false
        }
    }

    private fun initAdmob() {
        showOrLoadNativeAd()
    }

    private fun showOrLoadNativeAd() {
        activity?.let { act ->
            if ((NativeOnboard1.nativeAdHigh != null || NativeOnboard1.nativeAdNormal != null) && !isAdShown) {
                showAds()
            } else if (!isAdShown) {
                NativeOnboard1.getInstance().setCallbackNative(object : CallbackNative {
                    override fun onLoaded() {
                        if ((NativeOnboard1.nativeAdHigh != null || NativeOnboard1.nativeAdNormal != null) && !isAdShown) {
                            showAds()
                        }
                    }

                    override fun onFailed() {
                        mBinding.lnNative.invisibleView()
                    }

                    override fun onAdImpression() {}
                })
                NativeOnboard1.getInstance().loadAdsAll(act)
            }
        }
    }

    private fun showAds() {
        activity?.let { act ->
            NativeOnboard1.getInstance().showAdsAll(
                act,
                mBinding.lnNative,
                AdPlaceName.NATIVE_ONBOARD_1.name.lowercase()
            )
            isAdShown = true
        }
    }

    override fun onDestroyView() {
        NativeOnboard1.getInstance().destroyNativeAd()
        super.onDestroyView()
    }
}