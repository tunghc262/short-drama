package com.shortdrama.movie.views.fragments.onboard

import android.util.Log
import com.bumptech.glide.Glide
import com.module.ads.admob.inters.IntersOnboard
import com.module.ads.admob.natives.NativeOnboard3
import com.module.ads.admob.natives.NativeOnboardFullscreen2
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.FragmentOnboardBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.activities.onboard.OnboardActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.invisibleView
import com.shortdrama.movie.views.bases.ext.onClick
import com.shortdrama.movie.views.bases.ext.setTextById
import com.shortdrama.movie.views.bases.ext.visibleView


class Onboard2Fragment : BaseFragment<FragmentOnboardBinding>() {
    private var isFirstResume = true

    companion object {
        fun newInstance(): Onboard2Fragment {
            val fragment = Onboard2Fragment()
            return fragment
        }
    }

    override fun getLayoutFragment(): Int {
        return R.layout.fragment_onboard
    }

    override fun initViews() {
        super.initViews()
        Glide.with(this).load(R.drawable.img_onboard_2).into(mBinding.ivThumb)
        mBinding.tvTitle.setTextById(R.string.title_on_boarding_2)
        mBinding.tvContent.setTextById(R.string.content_on_boarding_2)
        mBinding.rlAdsFullscreen.goneView()
        mBinding.lnNative.invisibleView()
        mBinding.rlButton.visibleView()
        mBinding.lavHand.visibleView()

        mBinding.ivIndicator1.setImageResource(R.drawable.circle_indicator)
        mBinding.ivIndicator2.setImageResource(R.drawable.circle_indicator_selected)
        mBinding.ivIndicator3.setImageResource(R.drawable.circle_indicator)
        mBinding.tvNext.setTextById(R.string.text_next)

        activity?.let { act ->
            if (AppUtils.isSession2()) {
                logEvent("onboarding_2_ss_view")
            } else {
                logEvent("onboarding_2_view")
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
        Log.e("TAG", "onResume: Onboard 2")
        if (isFirstResume) {
            initAdmob()
            isFirstResume = false
            return
        }
    }

    private fun initAdmob() {
        activity?.let { act ->
            NativeOnboard3.getInstance().loadAdsAll(act)
            NativeOnboardFullscreen2.getInstance().loadAdsAll(act)
            IntersOnboard.getInstance().loadAdsAll(act)
        }
    }
}