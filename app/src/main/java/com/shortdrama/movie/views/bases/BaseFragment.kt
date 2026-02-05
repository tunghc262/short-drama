package com.shortdrama.movie.views.bases

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.module.ads.utils.FBTracking

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {
    lateinit var mBinding: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val layoutView = getLayoutFragment()
        mBinding = DataBindingUtil.inflate(inflater, layoutView, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        onClickViews()
        observerData()
    }

    abstract fun getLayoutFragment(): Int

    open fun initViews() {}

    open fun onClickViews() {}

    open fun observerData() {}

    fun logEvent(event: String, bundle: Bundle? = null) {
        activity?.let { act ->
            FBTracking.funcTracking(act, event.lowercase(), bundle)
        }
    }

    fun logTime(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putString("screen_name", fragment.javaClass.simpleName)
        bundle.putString("screen_class", fragment.javaClass.simpleName)
        FBTracking.funcTracking(activity, "screen_view", bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}