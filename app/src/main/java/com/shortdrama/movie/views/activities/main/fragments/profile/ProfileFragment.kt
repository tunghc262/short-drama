package com.shortdrama.movie.views.activities.main.fragments.profile

import android.content.Intent
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.FragmentProfileBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.ShareUtils
import com.shortdrama.movie.views.activities.language.LanguageSettingActivity
import com.shortdrama.movie.views.bases.BaseFragment
import com.shortdrama.movie.views.bases.ext.onClickAlpha
import com.shortdrama.movie.views.dialogs.FeedbackDialog
import com.shortdrama.movie.views.dialogs.RateAppDialog

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override fun getLayoutFragment(): Int = R.layout.fragment_profile
    override fun initViews() {
        super.initViews()
    }

    override fun onClickViews() {
        super.onClickViews()
        activity?.let { act ->
            mBinding.llButtonLanguage.onClickAlpha {
                val intent = Intent(act, LanguageSettingActivity::class.java)
                startActivity(intent)
            }
            mBinding.llButtonFeedback.onClickAlpha {
                FeedbackDialog(act).show()
            }
            mBinding.llButtonPrivacy.onClickAlpha {
                AppUtils.openBrowser(act, AppConstants.PRIVACY_POLICY_URL)
            }
            mBinding.llButtonRateUs.onClickAlpha {
                RateAppDialog(act).show()
            }
            mBinding.llButtonShareApp.onClickAlpha {
                ShareUtils.shareApp(act)
            }
        }
    }
}