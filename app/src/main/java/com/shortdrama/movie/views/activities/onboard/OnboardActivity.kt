package com.shortdrama.movie.views.activities.onboard


import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.module.ads.admob.natives.NativeOnboard1
import com.module.ads.admob.natives.NativeOnboard3
import com.module.ads.admob.natives.NativeOnboardFullscreen1
import com.module.ads.admob.natives.NativeOnboardFullscreen2
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.databinding.ActivityOnboardBinding
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.fragments.onboard.Onboard1Fragment
import com.shortdrama.movie.views.fragments.onboard.Onboard2Fragment
import com.shortdrama.movie.views.fragments.onboard.Onboard3Fragment
import com.shortdrama.movie.views.fragments.onboard.OnboardNativeFull1Fragment
import com.shortdrama.movie.views.fragments.onboard.OnboardNativeFull2Fragment


class OnboardActivity : BaseActivity<ActivityOnboardBinding>() {
    private var onboardAdapter: OnboardAdapter? = null

    private var countTimer1: CountDownTimer? = null
    private var countTimer2: CountDownTimer? = null

    override fun getLayoutActivity(): Int {
        return R.layout.activity_onboard
    }

    override fun initViews() {
        super.initViews()
        initList()
    }

    private fun initList() {
        var listFragment: ArrayList<Fragment>
        if (!PurchaseUtils.isNoAds(this) && FirebaseQuery.getEnableAds()) {
            if (FirebaseQuery.getEnableShowNativeFullScr1() && FirebaseQuery.getEnableShowNativeFullScr2()) {
                if (FirebaseQuery.getEnableNativeFullScr1() ||
                    FirebaseQuery.getEnableNativeFullScr2() ||
                    FirebaseQuery.getEnableNativeFullScr1High() ||
                    FirebaseQuery.getEnableNativeFullScr2High()
                ) {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        OnboardNativeFull1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        OnboardNativeFull2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                } else {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                }
            } else if (FirebaseQuery.getEnableShowNativeFullScr1()) {
                if (FirebaseQuery.getEnableNativeFullScr1() ||
                    FirebaseQuery.getEnableNativeFullScr1High()
                ) {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        OnboardNativeFull1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                } else {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                }
            } else if (FirebaseQuery.getEnableShowNativeFullScr2()) {
                if (FirebaseQuery.getEnableNativeFullScr2() ||
                    FirebaseQuery.getEnableNativeFullScr2High()
                ) {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        OnboardNativeFull2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                } else {
                    listFragment = arrayListOf(
                        Onboard1Fragment.newInstance(),
                        Onboard2Fragment.newInstance(),
                        Onboard3Fragment.newInstance()
                    )
                }
            } else {
                listFragment = arrayListOf(
                    Onboard1Fragment.newInstance(),
                    Onboard2Fragment.newInstance(),
                    Onboard3Fragment.newInstance()
                )
            }
        } else {
            listFragment = arrayListOf(
                Onboard1Fragment.newInstance(),
                Onboard2Fragment.newInstance(),
                Onboard3Fragment.newInstance()
            )
        }

        onboardAdapter = OnboardAdapter(this)
        onboardAdapter?.submitData(listFragment)
        mBinding.vpOnBoarding.apply {
            offscreenPageLimit = 1
            currentItem = 0
            adapter = onboardAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                private var lastPage = -1

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    countTimer1?.cancel()
                    countTimer2?.cancel()
                    NativeOnboardFullscreen1.getInstance().isShowing = false
                    NativeOnboardFullscreen2.getInstance().isShowing = false

                    if (!PurchaseUtils.isNoAds(this@OnboardActivity) && FirebaseQuery.getEnableAds()) {
                        if (FirebaseQuery.getEnableShowNativeFullScr1() && FirebaseQuery.getEnableShowNativeFullScr2()) {
                            // Nếu vừa từ NativeFull1 (1) sang OB2 (2) hoặc OB1 (0)
                            if (lastPage == 1 && (position == 0 || position == 2)) {
                                logEvent("native_ob_1_scr_complete")
                            }

                            // Nếu vừa từ NativeFull2 (3) sang OB2 (2) hoặc OB3 (4)
                            if (lastPage == 3 && (position == 2 || position == 4)) {
                                logEvent("native_ob_2_scr_complete")
                            }

                            lastPage = position

                            when (position) {
                                1, 3 -> {
                                    if (position == 1) {
                                        startCount1()
                                    }
                                    if (position == 3) {
                                        startCount2()
                                    }
                                }
                            }
                        } else if (FirebaseQuery.getEnableShowNativeFullScr1()) {
                            // Nếu vừa từ NativeFull1 (1) sang OB2 (2) hoặc OB1 (0)
                            if (lastPage == 1 && (position == 0 || position == 2)) {
                                logEvent("native_ob_1_scr_complete")
                            }
                            lastPage = position

                            when (position) {
                                1 -> {
                                    startCount1()
                                }
                            }
                        } else if (FirebaseQuery.getEnableShowNativeFullScr2()) {
                            // Nếu vừa từ NativeFull2 (3) sang OB2 (2) hoặc OB3 (4)
                            if (lastPage == 2 && (position == 1 || position == 3)) {
                                logEvent("native_ob_2_scr_complete")
                            }
                            lastPage = position

                            when (position) {
                                2 -> {
                                    startCount2()
                                }
                            }
                        }
                    }
                }
            })
        }
    }

    fun onNextPage() {
        onboardAdapter?.let {
            if (mBinding.vpOnBoarding.currentItem + 1 < it.itemCount) {
                mBinding.vpOnBoarding.currentItem += 1
            }
        }
    }

    private fun startCount1() {
        NativeOnboardFullscreen1.getInstance().isShowing = true
        var time = FirebaseQuery.getTimeScrollNativeFullScr()
        if (time == 0L) {
            time = 15000L
        }
        countTimer1 = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                onNextPage()
            }
        }
        countTimer1?.start()
    }

    private fun startCount2() {
        NativeOnboardFullscreen2.getInstance().isShowing = true
        var time = FirebaseQuery.getTimeScrollNativeFullScr()
        if (time == 0L) {
            time = 15000L
        }
        countTimer2 = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                onNextPage()
            }
        }
        countTimer2?.start()
    }

    override fun onBackPressed() {}

    override fun onDestroy() {
        NativeOnboard1.getInstance().destroyNativeAd()
        NativeOnboard3.getInstance().destroyNativeAd()
        NativeOnboardFullscreen1.getInstance().destroyNativeAd()
        NativeOnboardFullscreen2.getInstance().destroyNativeAd()
        super.onDestroy()
    }
}