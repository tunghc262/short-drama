package com.shortdrama.movie.views.activities.premium

import android.content.Intent
import android.graphics.Paint
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.PurchaseInfo
import com.anjlab.android.iab.v3.SkuDetails
import com.module.ads.utils.FBTracking
import com.module.ads.utils.PurchaseUtils
import com.shortdrama.movie.R
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.databinding.ActivityPremiumBinding
import com.shortdrama.movie.utils.AppUtils
import com.shortdrama.movie.utils.UsageManager
import com.shortdrama.movie.views.activities.main.MainActivity
import com.shortdrama.movie.views.bases.BaseActivity
import com.shortdrama.movie.views.bases.ext.showToastByString

private const val TAG = "PremiumActivity"

class PremiumActivity : BaseActivity<ActivityPremiumBinding>() {
    private var bp: BillingProcessor? = null
    private var isOnboarding = true
    var index = 0
    private var currentProductId = ""
    override fun getLayoutActivity(): Int {
        return R.layout.activity_premium
    }

    override fun initViews() {
        super.initViews()
        logEvent("sub_ob_view")
        isOnboarding = intent.getBooleanExtra(AppConstants.IS_FROM_ONBOARDING, false)
        initPurchase()
        mBinding.tvPriceOriginYearly.paintFlags = mBinding.tvPriceWeekly.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    override fun onClickViews() {
        super.onClickViews()
        mBinding.ivClose.setOnClickListener {
            if (isOnboarding) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        }
        mBinding.tvPrivacyPolicy.setOnClickListener {
            AppUtils.openBrowser(this, AppConstants.PRIVACY_POLICY_URL)
        }
        mBinding.tvTermsOfUse.setOnClickListener {
            AppUtils.openBrowser(this, AppConstants.TERM_OF_USE_URL)
        }

        mBinding.rlYearly.setOnClickListener {
            index = 0
            FBTracking.funcTracking(this, "sub_click_yearly", null)
            mBinding.rlYearly.setBackgroundResource(R.drawable.bg_btn_premium_selected)
            mBinding.rlMonthly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.rlWeekly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.ivCheckboxYearly.setImageResource(R.drawable.ic_checkbox_selected_iap)
            mBinding.ivCheckboxMonthly.setImageResource(R.drawable.ic_checkbox)
            mBinding.ivCheckboxWeekly.setImageResource(R.drawable.ic_checkbox)
        }
        mBinding.rlMonthly.setOnClickListener {
            index = 1
            FBTracking.funcTracking(this, "sub_click_monthly", null)
            mBinding.rlYearly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.rlMonthly.setBackgroundResource(R.drawable.bg_btn_premium_selected)
            mBinding.rlWeekly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.ivCheckboxYearly.setImageResource(R.drawable.ic_checkbox)
            mBinding.ivCheckboxMonthly.setImageResource(R.drawable.ic_checkbox_selected_iap)
            mBinding.ivCheckboxWeekly.setImageResource(R.drawable.ic_checkbox)
        }

        mBinding.rlWeekly.setOnClickListener {
            index = 2
            FBTracking.funcTracking(this, "sub_click_weekly", null)
            mBinding.rlYearly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.rlMonthly.setBackgroundResource(R.drawable.bg_btn_premium)
            mBinding.rlWeekly.setBackgroundResource(R.drawable.bg_btn_premium_selected)
            mBinding.ivCheckboxYearly.setImageResource(R.drawable.ic_checkbox)
            mBinding.ivCheckboxMonthly.setImageResource(R.drawable.ic_checkbox)
            mBinding.ivCheckboxWeekly.setImageResource(R.drawable.ic_checkbox_selected_iap)
        }

        mBinding.tvContinue.setOnClickListener {
            when (index) {
                0 -> {
                    purchaseSubscription(PurchaseUtils.getIdYear())
                }

                1 -> {
                    purchaseSubscription(PurchaseUtils.getIdMonth())
                }

                2 -> {
                    purchaseSubscription(PurchaseUtils.getIdWeek())
                }
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
                        when (productId) {
                            PurchaseUtils.getIdWeek() -> {
                                FBTracking.funcTracking(
                                    this@PremiumActivity,
                                    "sub_weekly_paydone",
                                    null
                                )
                                UsageManager.resetForPurchase(this@PremiumActivity)
                            }

                            PurchaseUtils.getIdMonth() -> {
                                FBTracking.funcTracking(
                                    this@PremiumActivity,
                                    "sub_monthly_paydone",
                                    null
                                )
                                UsageManager.resetForPurchase(this@PremiumActivity)
                            }

                            PurchaseUtils.getIdYear() -> {
                                FBTracking.funcTracking(
                                    this@PremiumActivity,
                                    "sub_yearly_paydone",
                                    null
                                )
                                UsageManager.resetForPurchase(this@PremiumActivity)
                            }
                        }
                        // Quay lại Main
                        finish()
                    }
                }

                override fun onPurchaseHistoryRestored() {}

                override fun onBillingError(errorCode: Int, error: Throwable?) {
                    val failedProduct = currentProductId
                    when (failedProduct) {
                        PurchaseUtils.getIdWeek() -> {
                            FBTracking.funcTracking(
                                this@PremiumActivity,
                                "sub_click_weekly_payfail",
                                null
                            )
                        }

                        PurchaseUtils.getIdMonth() -> {
                            FBTracking.funcTracking(
                                this@PremiumActivity,
                                "sub_click_monthly_payfail",
                                null
                            )
                        }

                        PurchaseUtils.getIdYear() -> {
                            FBTracking.funcTracking(
                                this@PremiumActivity,
                                "sub_click_yearly_payfail",
                                null
                            )
                        }
                    }
                }

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
                                                mBinding.tvPriceWeekly.text =
                                                    "${sku.priceText}/week"
                                            }

                                            PurchaseUtils.getIdMonth() -> {
                                                mBinding.tvPriceMonthly.text =
                                                    "${sku.priceText}/month"
                                            }

                                            PurchaseUtils.getIdYear() -> {
                                                mBinding.tvPriceYearly.text =
                                                    "${sku.priceLong / 52}/weekly"
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
        currentProductId = productId.toString()
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

    override fun onBackPressedCallback() {
        if (!isOnboarding){
            finish()
        }
    }
}