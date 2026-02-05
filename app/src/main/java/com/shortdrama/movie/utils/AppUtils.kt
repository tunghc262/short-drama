package com.shortdrama.movie.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import com.module.ads.utils.FBTracking
import com.shortdrama.movie.BuildConfig
import com.shortdrama.movie.app.AppConstants
import com.shortdrama.movie.views.bases.ext.getCurrentSdkVersion
import com.shortdrama.movie.views.bases.ext.showToastByString

object AppUtils {

    fun trackingEvent(context: Context, eventName: String = "", bundle: Bundle? = null) {
        if (isSession2()) {
            FBTracking.funcTracking(context, eventName, bundle)
        }
    }

    fun isSession2(): Boolean {
        return SharePrefUtils.getBoolean(AppConstants.KEY_SESSION_2, false)
    }

    fun sendEmailMore(
        context: Context,
        addresses: Array<String?>?,
        subject: String,
        body: String
    ) {
        disableExposure(context)
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            context.showToastByString("Install application email")
        }
    }

    private fun disableExposure(context: Context) {
        if (context.getCurrentSdkVersion() >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun openMarket(context: Context, packageName: String) {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            i.data = Uri.parse("market://details?id=$packageName")
            context.startActivity(i)
        } catch (ex: ActivityNotFoundException) {
            openBrowser(context, AppConstants.BASE_GOOGLE_PLAY + BuildConfig.APPLICATION_ID)
        }
    }

    fun openGoogleStore(mContext: Context, nameDeveloper: String) {
        try {
            val marketIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/developer?id=$nameDeveloper")
            )
            mContext.startActivity(marketIntent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun openBrowser(context: Context, url: String) {
        var mUrl = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            mUrl = "https://$url"
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mUrl))
        try {
            context.startActivity(browserIntent)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}