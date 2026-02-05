package com.shortdrama.movie.views.bases

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.module.ads.utils.FBTracking
import com.shortdrama.movie.R
import com.shortdrama.movie.utils.LanguageUtils

abstract class BaseDialog<VB : ViewDataBinding>(
    context: Context,
    themeResId: Int = R.style.ThemeDialog
) : Dialog(context, themeResId) {

    lateinit var mBinding: VB

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        createContentView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        LanguageUtils.setLocale(context)
        super.onCreate(savedInstanceState)
        window?.setLayout(
            ((context.resources.displayMetrics.widthPixels * 0.85).toInt()),
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        initViews()
        onClickViews()
    }

    private fun createContentView() {
        val layoutView = getLayoutDialog()
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutView, null, false)
        setContentView(mBinding.root)
    }

    abstract fun getLayoutDialog(): Int

    open fun initViews() {}

    open fun onClickViews() {}

    fun logEvent(event: String, bundle: Bundle? = null) {
        FBTracking.funcTracking(context, event.lowercase(), bundle)
    }
}