package com.shortdrama.movie.views.activities.language

import android.content.Intent
import androidx.activity.viewModels
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.data.models.LanguageModel
import com.shortdrama.movie.databinding.ActivityLanguageBinding
import com.shortdrama.movie.utils.LanguageUtils
import com.shortdrama.movie.utils.SharePrefUtils
import com.shortdrama.movie.views.activities.main.MainActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.goneView
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.bases.ext.visibleView


class LanguageSettingActivity : BaseActivity<ActivityLanguageBinding>() {
    private val mViewModel: LanguageViewModel by viewModels()
    private var languageAdapter: LanguageAdapter? = null
    private var listLanguage = arrayListOf<LanguageModel>()
    private var languageModel: LanguageModel? = null

    override fun getLayoutActivity(): Int {
        return R.layout.activity_language
    }

    override fun initViews() {
        super.initViews()
        mBinding.ivBack.visibleView()
        mBinding.lnNative.goneView()
        initList()
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.btnNext.onClickAlpha {
            onClickDone()
        }
        mBinding.ivBack.onClickAlpha {
            finish()
        }
    }

    private fun initList() {
        languageAdapter = LanguageAdapter(isLanguageSetting = true) { languageModel, position ->
            this.languageModel = languageModel
        }
        listLanguage.clear()
        listLanguage.addAll(mViewModel.listLanguageFull)

        val isChooseLanguage = SharePrefUtils.getBoolean(AppConstants.KEY_SELECT_LANGUAGE, false)
        val keyLanguageCode = SharePrefUtils.getString(AppConstants.KEY_LANGUAGE_CODE, "en")
        val keyCountryLanguage = SharePrefUtils.getString(AppConstants.KEY_LANGUAGE_COUNTRY, "")
        if (isChooseLanguage) {
            val model = listLanguage.findLast {
                keyLanguageCode == it.isoLanguage && keyCountryLanguage == it.countryLanguage
            }
            if (model != null) {
                val data = model.copy()
                data.isCheck = true
                listLanguage.remove(model)
                listLanguage.add(0, data)
                languageModel = data
                languageAdapter?.setSelectedItem(0)
            }
        } else {
            mViewModel.getLanguageDevice()?.let { language ->
                if (language in listLanguage) {
                    val data = language.copy()
                    listLanguage.remove(language)
                    listLanguage.add(0, data)
                    languageModel = data
                }
            }
        }

        languageAdapter?.submitData(listLanguage)

        mBinding.rvLanguage.apply {
            setHasFixedSize(true)
            adapter = languageAdapter
        }
    }

    private fun onClickDone() {
        SharePrefUtils.putBoolean(AppConstants.KEY_SELECT_LANGUAGE, true)
        languageModel?.let { model ->
            SharePrefUtils.putString(AppConstants.KEY_LANGUAGE_CODE, model.isoLanguage)
            SharePrefUtils.putString(AppConstants.KEY_LANGUAGE_COUNTRY, model.countryLanguage)
        }
        LanguageUtils.setLocale(this)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

}