package com.shortdrama.movie.views.activities.language

import android.content.res.Resources
import android.os.Build
import androidx.lifecycle.ViewModel
import com.shortdrama.movie.R
import com.shortdrama.movie.data.models.LanguageModel

class LanguageViewModel : ViewModel() {
    val listLanguageFull = arrayListOf(
        LanguageModel("English", "en"),
        LanguageModel("English (AU)", "en", "AU"),
        LanguageModel("English (CA)", "en", "CA"),
        LanguageModel("English (UK)", "en", "GB"),
        LanguageModel("English (IN)", "en", "IN"),
        LanguageModel("English (SA)", "en", "SA"),
        LanguageModel("Español", "es"),
        LanguageModel("Español (Argentina)", "es", "AR"),
        LanguageModel("Español (México)", "es", "MX"),
        LanguageModel("Español (US)", "es", "US"),
        LanguageModel("Français", "fr"),
        LanguageModel("Français (CA)", "fr", "CA"),
        LanguageModel("Português", "pt"),
        LanguageModel("Português (Brasil)", "pt", "BR"),
        LanguageModel("Deutsch", "de"),
        LanguageModel("Türkçe", "tr"),
        LanguageModel("हिन्दी", "hi"),
        LanguageModel("ಕನ್ನಡ", "kn"),
        LanguageModel("తెలుగు", "te"),
        LanguageModel("தமிழ்", "ta"),
        LanguageModel("ગુજરાતી", "gu"),
        LanguageModel("मराठी", "mr"),
        LanguageModel("ਪੰਜਾਬੀ", "pa"),
        LanguageModel("മലയാളം", "ml"),
        LanguageModel("한국어", "ko"),
        LanguageModel("日本語", "ja"),
        LanguageModel("Bahasa Indonesia", "id"),
        LanguageModel("ไทย", "th"),
        LanguageModel("العربية", "ar"),
        LanguageModel("اردو", "ur"),
        LanguageModel("فارسی", "fa"),
        LanguageModel("עברית", "he"),
        LanguageModel("Български", "bg"),
        LanguageModel("বাংলা", "bn"),
        LanguageModel("Català", "ca"),
        LanguageModel("Čeština", "cs"),
        LanguageModel("Dansk", "da"),
        LanguageModel("Ελληνικά", "el"),
        LanguageModel("Eesti", "et"),
        LanguageModel("Suomi", "fi"),
        LanguageModel("Filipino", "fil"),
        LanguageModel("Hrvatski", "hr"),
        LanguageModel("Magyar", "hu"),
        LanguageModel("Íslenska", "is"),
        LanguageModel("Italiano", "it"),
        LanguageModel("Lietuvių", "lt"),
        LanguageModel("Latviešu", "lv"),
        LanguageModel("Bahasa Melayu", "ms"),
        LanguageModel("မြန်မာစာ", "my"),
        LanguageModel("Nederlands", "nl"),
        LanguageModel("Norsk", "no"),
        LanguageModel("Polski", "pl"),
        LanguageModel("Română", "ro"),
        LanguageModel("Slovenčina", "sk"),
        LanguageModel("Slovenščina", "sl"),
        LanguageModel("Српски", "sr"),
        LanguageModel("Svenska", "sv"),
        LanguageModel("Українська", "uk"),
        LanguageModel("Русский", "ru"),
        LanguageModel("简体中文（中国）", "zh"),
        LanguageModel("繁體中文 (香港)", "zh", "HK"),
        LanguageModel("简体中文 (新加坡)", "zh", "SG"),
        LanguageModel("繁體中文 (台灣)", "zh", "TW"),
        LanguageModel("Oʻzbekcha", "uz"),
        LanguageModel("Avañe'ẽ", "gn"),
        LanguageModel("Azərbaycan dili", "az"),
        LanguageModel("Қазақ тілі", "kk"),
        LanguageModel("Tiếng Việt", "vi"),
    )

    val listLanguage = arrayListOf(
        LanguageModel("English", "en", image = R.drawable.ic_language_english),
        LanguageModel("Spanish", "es", image = R.drawable.ic_language_spanish),
        LanguageModel("French", "fr", image = R.drawable.ic_language_france),
        LanguageModel("Hindi", "hi", image = R.drawable.ic_language_hindi),
        LanguageModel("Indonesian", "in", image = R.drawable.ic_language_indonesian),
        LanguageModel("Portuguese", "pt", image = R.drawable.ic_language_portugal),
        LanguageModel("Turkish", "tr", image = R.drawable.ic_language_turkish),
        LanguageModel("Vietnamese", "vi", image = R.drawable.ic_language_vietnamese)
    )

    fun getLanguageDevice(): LanguageModel? {
        val language: String
        val country: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            language = Resources.getSystem().configuration.locales[0].language
            country = Resources.getSystem().configuration.locales[0].country
        } else {
            language = Resources.getSystem().configuration.locale.language
            country = Resources.getSystem().configuration.locale.country
        }
        val modelFull = listLanguageFull.findLast {
            it.isoLanguage.equals(language, ignoreCase = true) &&
                    it.countryLanguage.equals(country, ignoreCase = true)
        }
        val model = listLanguageFull.findLast {
            it.isoLanguage.equals(language, ignoreCase = true)
        }
        return modelFull ?: model
    }

    private fun getLanguageApp() = arrayListOf(
        "de",
        "el",
        "gn",
        "gu",
        "he",
        "hi",
        "hu",
        "is",
        "id",
        "it",
        "ja",
        "kn",
        "kk",
        "ko",
        "lv",
        "lt",
        "ms",
        "ml",
        "mr",
        "no",
        "fa",
        "pl",
        "pt",
        "pt-BR",
        "pa",
        "ro",
        "ar",
        "ru",
        "az",
        "bn",
        "sr",
        "bg",
        "sk",
        "sl",
        "my",
        "ca",
        "es",
        "es-AR",
        "es-MX",
        "es-US",
        "zh",
        "zh-HK",
        "zh-SG",
        "zh-TW",
        "sv",
        "hr",
        "ta",
        "cs",
        "te",
        "th",
        "da",
        "tr",
        "nl",
        "uk",
        "en",
        "en-AU",
        "en-CA",
        "en-GB",
        "en-IN",
        "en-SA",
        "ur",
        "uz",
        "vi",
        "et",
        "fil",
        "fi",
        "fr",
        "fr-CA",
    )
}