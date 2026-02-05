package com.shortdrama.movie.views.activities.premium

import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivityPremiumBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.UsageManager
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.showToastByString

private const val TAG = "PremiumActivity"

class PremiumActivity : BaseActivity<ActivityPremiumBinding>() {
    private var isWeekly = true
    private var bp: BillingProcessor? = null

    override fun getLayoutActivity(): Int {
        return R.layout.activity_premium
    }

    override fun initViews() {
        super.initViews()
        initPurchase()
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.setOnClickListener {
            finish()
        }
        mBinding.tvPrivacyPolicy.setOnClickListener {
            AppUtils.openBrowser(this, AppConstants.PRIVACY_POLICY_URL)
        }
        mBinding.tvTermsOfUse.setOnClickListener {
            AppUtils.openBrowser(this, AppConstants.TERM_OF_USE_URL)
        }

        mBinding.rlWeekly.setOnClickListener {
            isWeekly = true
            mBinding.rlMonthly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.rlWeekly.setBackgroundResource(R.drawable.bg_btn_premium_selected)
            mBinding.tvPremium1.text = "210 free images, 30 images per day"
            mBinding.ivCheckboxWeekly.setImageResource(R.drawable.ic_checkbox_selected)
            mBinding.ivCheckboxMonthly.setImageResource(R.drawable.ic_checkbox)
        }
        mBinding.rlMonthly.setOnClickListener {
            isWeekly = false
            mBinding.rlMonthly.setBackgroundResource(R.drawable.bg_btn_premium_selected)
            mBinding.rlWeekly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.tvPremium1.text = "1500 free images, 50 images per day"
            mBinding.ivCheckboxMonthly.setImageResource(R.drawable.ic_checkbox_selected)
            mBinding.ivCheckboxWeekly.setImageResource(R.drawable.ic_checkbox)
        }
        mBinding.tvContinue.setOnClickListener {
            if (isWeekly) {
                purchaseSubscription(PurchaseUtils.getIdWeek())
            } else {
                purchaseSubscription(PurchaseUtils.getIdMonth())
            }
        }
    }

    private fun initPurchase() {
        bp = BillingProcessor.newBillingProcessor(
            this,
            PurchaseUtils.LICENSE_KEY,
            object : BillingProcessor.IBillingHandler {
                override fun onProductPurchased(
                    productId: String,
                    details: PurchaseInfo?
                ) {
                    runOnUiThread {
                        if (productId == PurchaseUtils.getIdWeek()) {
                            //UsageManager.setActivePlan(this@PremiumActivity, "sub_week")
                            UsageManager.resetForPurchase(this@PremiumActivity)
                        } else if (productId == PurchaseUtils.getIdMonth()) {
                            //UsageManager.setActivePlan(this@PremiumActivity, "sub_month")
                            UsageManager.resetForPurchase(this@PremiumActivity)
                        }
                        // Quay lại Main
                        finish()
                    }
                }

                override fun onPurchaseHistoryRestored() {}

                override fun onBillingError(errorCode: Int, error: Throwable?) {}

                override fun onBillingInitialized() {
                    val subscriptionIds =
                        arrayListOf(PurchaseUtils.getIdWeek(), PurchaseUtils.getIdMonth())
                    bp?.getSubscriptionsListingDetailsAsync(
                        subscriptionIds,
                        object : BillingProcessor.ISkuDetailsResponseListener {
                            override fun onSkuDetailsResponse(skuDetails: List<SkuDetails>?) {
                                if (skuDetails == null || skuDetails.isEmpty()) {
                                    // Lỗi → dùng giá mặc định
                                    return
                                }

                                runOnUiThread {
                                    for (sku in skuDetails) {
                                        when (sku.productId) {
                                            PurchaseUtils.getIdWeek() -> {
                                                mBinding.tvPriceWeekly.text = sku.priceText
                                            }

                                            PurchaseUtils.getIdMonth() -> {
                                                mBinding.tvPriceMonthly.text = sku.priceText
                                            }
                                        }
                                    }
                                }
                            }

                            override fun onSkuDetailsError(error: String?) {
                                runOnUiThread {

                                }
                            }
                        })
                }
            })
        bp?.initialize()
    }

    private fun purchaseSubscription(productId: String?) {
        if (bp == null || bp?.isInitialized == false) {
            showToastByString("Billing is not ready yet. Please try again.")
            return
        }

        // Kiểm tra dịch vụ có sẵn không
        if (!BillingProcessor.isIabServiceAvailable(this)) {
            showToastByString("Google Play Billing is not available on this device.")
            return
        }

        // Gọi subscription flow
        val launched = bp?.subscribe(this, productId)

        if (launched == true) {
            showToastByString("Processing payment...")
        } else {
            showToastByString("Unable to open Google Play. Please check your connection.")
        }
    }

    override fun onDestroy() {
        bp?.release()
        super.onDestroy()
    }
}