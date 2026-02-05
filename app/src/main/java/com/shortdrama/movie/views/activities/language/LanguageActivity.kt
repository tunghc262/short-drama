package com.shortdrama.movie.views.activities.language

import android.content.Intent
import androidx.activity.viewModels
import com.module.ads.admob.aoa.ResumeAdsManager
import com.module.ads.admob.natives.NativeLanguageAll
import com.module.ads.admob.natives.NativeLanguageDupAll
import com.module.ads.callback.CallbackNative
import com.module.ads.remote.FirebaseQuery
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AdPlaceName
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.models.LanguageModel
import com.shortdrama.movie.databinding.ActivityLanguageBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView

class LanguageActivity : BaseActivity<ActivityLanguageBinding>() {
    private val mViewModel: LanguageViewModel by viewModels()
    private var languageAdapter: LanguageAdapter? = null
    private var listLanguage = arrayListOf<LanguageModel>()

    private var isAdShown = false
    private var isFirstResume = true

    override fun getLayoutActivity(): Int {
        return R.layout.activity_language
    }

    override fun initViews() {
        super.initViews()
        initAdmob()
        initList()
        mBinding.ivDone.alpha = 0.5f
        mBinding.ivDone.isEnabled = false

        if (AppUtils.isSession2()) {
            logEvent("language_1_ss_view")
        } else {
            logEvent("language_1_view")
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
            NativeLanguageAll.getInstance().destroyAd() // Xóa quảng cáo cũ
            showOrLoadNativeAd() // Load và hiển thị quảng cáo mới
        } else {
            ResumeAdsManager.shouldReloadAd = false
        }
    }

    private fun initAdmob() {
        showOrLoadNativeAd()
        // load native dup
        NativeLanguageDupAll.getInstance().loadAdsAll(this)
    }

    private fun showOrLoadNativeAd() {
        if ((NativeLanguageAll.nativeAdHigh != null || NativeLanguageAll.nativeAdNormal != null) && !isAdShown) {
            NativeLanguageAll.getInstance().showAdsAll(
                this,
                mBinding.lnNative,
                AdPlaceName.NATIVE_LANGUAGE.name.lowercase()
            )
            isAdShown = true
        } else if (!isAdShown) {
            NativeLanguageAll.getInstance().setCallbackNative(object : CallbackNative {
                override fun onLoaded() {
                    if (!isAdShown && (NativeLanguageAll.nativeAdHigh != null || NativeLanguageAll.nativeAdNormal != null)) {
                        NativeLanguageAll.getInstance().showAdsAll(
                            this@LanguageActivity,
                            mBinding.lnNative,
                            AdPlaceName.NATIVE_LANGUAGE.name.lowercase()
                        )
                    }
                }

                override fun onFailed() {
                    mBinding.lnNative.goneView()
                }

                override fun onAdImpression() {}
            })
            NativeLanguageAll.getInstance().loadAdsAll(this)
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

        languageAdapter = LanguageAdapter() { languageModel, position ->
            logEvent("language_1_click_language")
            val intent = Intent(this@LanguageActivity, LanguageDupActivity::class.java)
            intent.putExtra(AppConstants.OBJ_LANGUAGE, languageModel)
            intent.putExtra(AppConstants.POSITION_LANGUAGE_SELECTED, position)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }
        languageAdapter?.submitData(listLanguage)

        mBinding.rvLanguage.apply {
            setHasFixedSize(true)
            adapter = languageAdapter
        }
    }

    override fun onBackPressed() {}

    override fun onDestroy() {
        NativeLanguageAll.getInstance().destroyAd()
        super.onDestroy()
    }
}