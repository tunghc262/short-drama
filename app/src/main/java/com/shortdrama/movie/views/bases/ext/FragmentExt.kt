package com.shortdrama.movie.views.bases.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

fun getTopFragment(fragmentActivity: FragmentActivity): Fragment? {
    var fragment: Fragment? = null
    val childCount = fragmentActivity.supportFragmentManager.backStackEntryCount
    if (childCount > 0) {
        val tag = fragmentActivity.supportFragmentManager.getBackStackEntryAt(childCount - 1).name
        fragment = fragmentActivity.supportFragmentManager.findFragmentByTag(tag)
    }
    return fragment
}

private fun clearTopFragment(fm: FragmentManager?) {
    fm?.let {
        val count = it.backStackEntryCount
        for (i in 0 until count) {
            val tag = it.getBackStackEntryAt(count - 1).name
            var f = it.findFragmentByTag(tag)
            it.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }
}

fun Fragment.replaceFragment(fragmentActivity: FragmentActivity, containerViewId: Int) {
    val tag = this::class.simpleName
    fragmentActivity.supportFragmentManager
        .beginTransaction()
        .replace(containerViewId, this, tag)
        .addToBackStack(tag)
        .commitAllowingStateLoss()
}

fun Fragment.addFragment(fragmentActivity: FragmentActivity, containerViewId: Int) {
    val tag = this::class.simpleName
    fragmentActivity.supportFragmentManager
        .beginTransaction()
        .add(containerViewId, this, tag)
        .addToBackStack(tag)
        .commitAllowingStateLoss()
}