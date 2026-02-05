package com.shortdrama.movie.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import com.shortdrama.movie.app.AppConstants
import java.util.Locale

object LanguageUtils {
    fun setLocale(context: Context): Context {
        val language = SharePrefUtils.getString(AppConstants.KEY_LANGUAGE_CODE, "en")
        val country = SharePrefUtils.getString(AppConstants.KEY_LANGUAGE_COUNTRY, "")
        val locale = Locale(language, country)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            config.setLocales(LocaleList(locale))
            return context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return context
        }
    }
}