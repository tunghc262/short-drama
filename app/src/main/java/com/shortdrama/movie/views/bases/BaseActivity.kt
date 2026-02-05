package com.shortdrama.movie.views.bases

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.module.ads.utils.FBTracking
import com.shortdrama.movie.app.GlobalApp
import com.shortdrama.movie.notification.ServiceNotificationManager
import com.shortdrama.movie.utils.LanguageUtils

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    lateinit var mBinding: VB
    var serviceNotificationManager: ServiceNotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LanguageUtils.setLocale(this)

        val layoutView = getLayoutActivity()
        if (!::mBinding.isInitialized) {
            mBinding = DataBindingUtil.setContentView(this, layoutView)
        }
        mBinding.lifecycleOwner = this

        serviceNotificationManager = (application as? GlobalApp)?.serviceNotificationManager

        initViews()
        onClickViews()
        observerData()
        val backCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedCallback()
            }
        }
        onBackPressedDispatcher.addCallback(this, backCallback)
    }

    open fun onBackPressedCallback() {
        finish()
    }

    abstract fun getLayoutActivity(): Int

    open fun initViews() {}

    open fun onClickViews() {}

    open fun observerData() {}

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        hideNavigationBar()
    }

    private fun hideNavigationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).let { controller ->
                controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            hideSystemUIBelowR()
        }
    }

    private fun hideSystemUIBelowR() {
        val decorView: View = window.decorView
        val uiOptions = decorView.systemUiVisibility
        var newUiOptions = uiOptions
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE
        newUiOptions = newUiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = newUiOptions
    }

    override fun attachBaseContext(newBase: Context) {
        val localeUpdatedContext = LanguageUtils.setLocale(newBase)
        super.attachBaseContext(localeUpdatedContext)
    }

    fun logEvent(event: String, bundle: Bundle? = null) {
        FBTracking.funcTracking(this, event, bundle)
    }
}