package com.shortdrama.movie.views.fragments.onboard

import android.content.Intent
import com.bumptech.glide.Glide
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.inters.IntersOnboard
import com.module.ads.admob.natives.NativeOnboard3
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentOnboardBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.activities.main.MainActivity
import com.shortdrama.movie.views.activities.premium.PremiumActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.setTextById

class Onboard3Fragment : BaseFragment<FragmentOnboardBinding>() {

    private var isAdShown = false
    private var isFirstResume = true

    companion object {
        fun newInstance(): Onboard3Fragment {
            val fragment = Onboard3Fragment()
            return fragment
        }
    }

    override fun getLayoutFragment(): Int {
        return R.layout.fragment_onboard
    }

    override fun initViews() {
        super.initViews()
        Glide.with(this).load(R.drawable.img_onboard_3).into(mBinding.ivThumb)
        mBinding.tvTitle.setTextById(R.string.title_on_boarding_3)
        mBinding.tvContent.setTextById(R.string.content_on_boarding_3)
        mBinding.rlAdsFullscreen.goneView()
        mBinding.lavHand.goneView()

        mBinding.ivIndicator1.setImageResource(R.drawable.circle_indicator)
        mBinding.ivIndicator2.setImageResource(R.drawable.circle_indicator)
        mBinding.ivIndicator3.setImageResource(R.drawable.circle_indicator_selected)
        mBinding.tvNext.setTextById(R.string.text_get_started)

        activity?.let { act ->
            if (AppUtils.isSession2()) {
                logEvent("onboarding_3_ss_view")
            } else {
                logEvent("onboarding_3_view")
            }
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.tvNext.setOnClickListener {
            logEvent("onboarding_3_start_click")
            onNextActivity()
        }
    }

    override fun onResume() {
        super.onResume()

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
            NativeOnboard3.getInstance().destroyNativeAd() // Xóa quảng cáo cũ
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
            if ((NativeOnboard3.nativeAdHigh != null || NativeOnboard3.nativeAdNormal != null) && !isAdShown) {
                showAds()
            } else if (!isAdShown) {
                NativeOnboard3.getInstance().setCallbackNative(object : CallbackNative {
                    override fun onLoaded() {
                        if ((NativeOnboard3.nativeAdHigh != null || NativeOnboard3.nativeAdNormal != null) && !isAdShown) {
                            showAds()
                        }
                    }

                    override fun onFailed() {
                        mBinding.lnNative.invisibleView()
                    }

                    override fun onAdImpression() {}
                })
                NativeOnboard3.getInstance().loadAdsAll(act)
            }
        }
    }

    private fun showAds() {
        activity?.let { act ->
            NativeOnboard3.getInstance().showAdsAll(
                act,
                mBinding.lnNative,
                AdPlaceName.NATIVE_ONBOARD_3.name.lowercase()
            )
            isAdShown = true
        }
    }

    private fun onNextActivity() {
        activity?.let { act ->
            if (FirebaseQuery.getEnableIap() && !PurchaseUtils.isNoAds(context) && FirebaseQuery.getEnableAds()) {
                IntersOnboard.getInstance().showAds(act) {
                    val intent = Intent(act, PremiumActivity::class.java)
                    intent.putExtra(AppConstants.IS_FROM_ONBOARDING, true)
                    startActivity(intent)
                    act.finish()
                }
            } else {
                IntersOnboard.getInstance().showAds(act) {
                    val intent = Intent(act, MainActivity::class.java)
                    startActivity(intent)
                    act.finish()
                }
            }
        }
    }

    override fun onDestroyView() {
        NativeOnboard3.getInstance().destroyNativeAd()
        super.onDestroyView()
    }
}