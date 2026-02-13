package com.shortdrama.movie.views.activities.language

import android.content.Intent
import androidx.activity.viewModels
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.natives.NativeLanguageDupAll
import com.module.ads.admob.natives.NativeOnboard1
import com.module.ads.admob.natives.NativeOnboardFullscreen1
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.models.LanguageModel
import com.shortdrama.movie.databinding.ActivityLanguageBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.LanguageUtils
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.activities.onboard.OnboardActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.parcelable


class LanguageDupActivity : BaseActivity<ActivityLanguageBinding>() {
    private val mViewModel: LanguageViewModel by viewModels()
    private var languageAdapter: LanguageAdapter? = null
    private var listLanguage = arrayListOf<LanguageModel>()
    private var languageModel: LanguageModel? = null
    private var positionLanguageSelected = -1

    private var isAdShown = false
    private var isFirstResume = true

    private var adShowStartTime: Long = 0L

    override fun getLayoutActivity(): Int {
        return R.layout.activity_language
    }

    override fun initViews() {
        super.initViews()
        intent?.let {
            if (it.hasExtra(AppConstants.POSITION_LANGUAGE_SELECTED)) {
                positionLanguageSelected =
                    it.getIntExtra(AppConstants.POSITION_LANGUAGE_SELECTED, -1)
                languageModel = it.parcelable<LanguageModel>(AppConstants.OBJ_LANGUAGE)
            }
        }

        initAdmob()
        initList()

        if (AppUtils.isSession2()) {
            logEvent("language_2_ss_view")
        } else {
            logEvent("language_2_view")
        }
    }

    override fun onResume() {
        super.onResume()
        if (PurchaseUtils.isNoAds(this) || !FirebaseQuery.getEnableAds()) {
            mBinding.lnNative.goneView()
        }
        // Bỏ qua lần onResume đầu tiên ngay sau onCreate
        if (isFirstResume) {
            isFirstResume = false
            return
        }

        if (!ResumeAdsManager.shouldReloadAd) {
            // Load lại và hiển thị quảng cáo mới trong onResume
            isAdShown = false // Reset để cho phép hiển thị quảng cáo mới
            NativeLanguageDupAll.getInstance().destroyAd() // Xóa quảng cáo cũ
            showOrLoadNativeAdDup() // Load và hiển thị quảng cáo mới
        } else {
            ResumeAdsManager.shouldReloadAd = false
        }
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.btnNext.setOnClickListener {
            logEvent("language_2_click_save")
            onClickDone()
        }
    }

    private fun initAdmob() {
        showOrLoadNativeAdDup()
        NativeOnboard1.getInstance().loadAdsAll(this)
        NativeOnboardFullscreen1.getInstance().loadAdsAll(this)
    }

    private fun showOrLoadNativeAdDup() {
        if ((NativeLanguageDupAll.nativeAdHigh != null || NativeLanguageDupAll.nativeAdNormal != null) && !isAdShown) {
            adShowStartTime = System.currentTimeMillis()
            NativeLanguageDupAll.getInstance().showAdsAll(
                this,
                mBinding.lnNative,
                AdPlaceName.NATIVE_LANGUAGE_DUP.name.lowercase()
            )
            isAdShown = true
        } else if (!isAdShown) {
            NativeLanguageDupAll.getInstance().setCallbackNative(object : CallbackNative {
                override fun onLoaded() {
                    if (!isAdShown && (NativeLanguageDupAll.nativeAdHigh != null || NativeLanguageDupAll.nativeAdNormal != null)) {
                        NativeLanguageDupAll.getInstance().showAdsAll(
                            this@LanguageDupActivity,
                            mBinding.lnNative,
                            AdPlaceName.NATIVE_LANGUAGE_DUP.name.lowercase()
                        )
                    }
                }

                override fun onFailed() {
                    mBinding.lnNative.goneView()
                }

                override fun onAdImpression() {
                    adShowStartTime = System.currentTimeMillis()
                }
            })
            NativeLanguageDupAll.getInstance().loadAdsAll(this)
        }
    }

    private fun initList() {
        listLanguage.clear()
        listLanguage.addAll(mViewModel.listLanguageFull)

        mViewModel.getLanguageDevice()?.let { language ->
            if (language in listLanguage) {
                val data = language.copy()
                listLanguage.remove(language)
                listLanguage.add(0, data)
            }
        }

        languageAdapter = LanguageAdapter(isLanguageDup = true) { languageModel, position ->
            this.languageModel = languageModel
        }
        languageAdapter?.submitData(listLanguage)
        languageAdapter?.setSelectedItem(positionLanguageSelected)

        mBinding.rvLanguage.apply {
            setHasFixedSize(true)
            adapter = languageAdapter
        }
    }

    private fun onClickDone() {
        languageModel?.let { model ->
            SharePrefUtils.putString(AppConstants.KEY_LANGUAGE_CODE, model.isoLanguage)
            SharePrefUtils.putString(AppConstants.KEY_LANGUAGE_COUNTRY, model.countryLanguage)
        }
        LanguageUtils.setLocale(this)
        val intent = Intent(this@LanguageDupActivity, OnboardActivity::class.java)
        intent.putExtra(AppConstants.OBJ_LANGUAGE, languageModel)
        startActivity(intent)
        finish()
    }

    private fun handleItemClick() {
        if (adShowStartTime == 0L) return
        val elapsedSeconds = (System.currentTimeMillis() - adShowStartTime) / 1000
        if (elapsedSeconds >= 3) {
            // Ads đã hiển thị >= 3s → load lại
            isAdShown = false // Reset để cho phép hiển thị quảng cáo mới
            NativeLanguageDupAll.getInstance().destroyAd() // Xóa quảng cáo cũ
            showOrLoadNativeAdDup() // Load và hiển thị quảng cáo mới
        }
    }

    override fun onBackPressedCallback() {}

    override fun onDestroy() {
        NativeLanguageDupAll.getInstance().destroyAd()
        super.onDestroy()
    }
}