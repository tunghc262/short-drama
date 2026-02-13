package com.module.ads.remote;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchThrottledException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.module.ads.admob.aoa.OpenAdsManager;
import com.module.ads.admob.banner.BannerSplash;
import com.module.ads.admob.inters.IntersSplashAll;
import com.module.ads.admob.natives.NativeLanguageAll;
import com.module.ads.callback.CallbackAd;
import com.module.ads.callback.CallbackBanner;
import com.module.ads.callback.CallbackTimeout;
import com.module.ads.models.ConfigUpdate;
import com.module.ads.models.config.ConfigAd;
import com.module.ads.models.config.ConfigAdPlaceNative;
import com.module.ads.utils.PurchaseUtils;
import com.module.ads.utils.SharePreferUtils;

import java.util.HashMap;
import java.util.Map;

public class FirebaseQuery {
    public static final String TIME_SHOW_INTERS = "time_show_inters";
    public static final String TIME_SCROLL_NATIVE_FULL_SCR = "time_scroll_native_full_scr";

    public static final String ID_INTERS_SPLASH = "inter_splash";
    public static final String ID_INTERS_SPLASH_HIGH = "inter_splash_high";
    public static final String ID_INTERS_SPLASH_2_HIGH = "inter_splash_2_high";
    public static final String ID_INTERS_SPLASH_3_HIGH = "inter_splash_3_high";

    public static final String ID_INTERS_IN_APP = "inter_in_app";
    public static final String ID_INTERS_IN_APP_HIGH = "inter_in_app_high";

    public static final String ID_NATIVE_LANGUAGE = "native_language";
    public static final String ID_NATIVE_LANGUAGE_HIGH = "native_language_high";
    public static final String ID_NATIVE_LANGUAGE_DUP = "native_language_dup";
    public static final String ID_NATIVE_LANGUAGE_DUP_HIGH = "native_language_dup_high";
    public static final String ID_NATIVE_ONBOARD_1 = "native_onboard_1";
    public static final String ID_NATIVE_ONBOARD_3 = "native_onboard_3";
    public static final String ID_NATIVE_ONBOARD_1_HIGH = "native_onboard_1_high";
    public static final String ID_NATIVE_ONBOARD_3_HIGH = "native_onboard_3_high";
    public static final String ID_NATIVE_FULL_ONBOARD_1 = "native_full_scr_1";
    public static final String ID_NATIVE_FULL_ONBOARD_2 = "native_full_scr_2";
    public static final String ID_NATIVE_FULL_ONBOARD_1_HIGH = "native_full_scr_1_high";
    public static final String ID_NATIVE_FULL_ONBOARD_2_HIGH = "native_full_scr_2_high";
    public static final String ID_INTERS_ONBOARD = "inter_onboard";
    public static final String ID_INTERS_ONBOARD_HIGH = "inter_onboard_high";

    public static final String ID_NATIVE_IN_APP = "native_in_app";
    public static final String ID_NATIVE_MAIN = "native_main";

    public static final String ID_OPEN_START = "open_start";
    public static final String ID_OPEN_RESUME = "open_resume";

    public static final String ID_BANNER_SPLASH = "banner_splash";
    public static final String ID_BANNER_IN_APP = "banner_in_app";

    public static final String ID_REWARD_IN_APP = "reward_in_app";

    public static final String ENABLE_ADS = "enable_ads";
    public static final String ENABLE_OPEN_RESUME = "enable_open_resume";
    public static final String ENABLE_OPEN_START = "enable_open_start";
    public static final String ENABLE_BANNER = "enable_banner";
    public static final String ENABLE_NATIVE = "enable_native";
    public static final String ENABLE_INTERS = "enable_inters";
    public static final String ENABLE_REWARD = "enable_reward";
    public static final String ENABLE_BANNER_COLLAPSE = "enable_banner_collapsible";
    public static final String ENABLE_NATIVE_COLLAPSE = "enable_native_collapsible";

    public static final String IS_SHOW_LANGUAGE_REOPEN = "is_show_language_reopen";
    public static final String IS_SHOW_OPEN_START = "is_show_open_start";

    public static final String CONFIG_ADS = "config_ads";
    public static final String CONFIG_UPDATE = "config_update";

    public static final String ENABLE_BANNER_SPLASH = "enable_banner_splash";

    public static final String ENABLE_INTERS_SPLASH = "enable_inter_splash";
    public static final String ENABLE_INTERS_SPLASH_HIGH = "enable_inter_splash_high";
    public static final String ENABLE_INTERS_IN_APP = "enable_inters_in_app";
    public static final String ENABLE_INTERS_IN_APP_HIGH = "enable_inters_in_app_high";

    public static final String ENABLE_NATIVE_LANGUAGE = "enable_native_language";
    public static final String ENABLE_NATIVE_LANGUAGE_HIGH = "enable_native_language_high";
    public static final String ENABLE_NATIVE_LANGUAGE_DUP = "enable_native_language_dup";
    public static final String ENABLE_NATIVE_LANGUAGE_DUP_HIGH = "enable_native_language_dup_high";
    public static final String ENABLE_NATIVE_ONBOARD_1 = "enable_native_onboard_1";
    public static final String ENABLE_NATIVE_ONBOARD_3 = "enable_native_onboard_3";
    public static final String ENABLE_NATIVE_ONBOARD_1_HIGH = "enable_native_onboard_1_high";
    public static final String ENABLE_NATIVE_ONBOARD_3_HIGH = "enable_native_onboard_3_high";
    public static final String ENABLE_NATIVE_FULL_SCR_1 = "enable_native_full_scr_1";
    public static final String ENABLE_NATIVE_FULL_SCR_2 = "enable_native_full_scr_2";
    public static final String ENABLE_NATIVE_FULL_SCR_1_HIGH = "enable_native_full_scr_1_high";
    public static final String ENABLE_NATIVE_FULL_SCR_2_HIGH = "enable_native_full_scr_2_high";
    public static final String ENABLE_INTERS_ONBOARD = "enable_inters_onboard";
    public static final String ENABLE_INTERS_ONBOARD_HIGH = "enable_inters_onboard_high";

    public static final String ENABLE_NATIVE_IN_APP = "enable_native_in_app";

    public static final String ENABLE_SHOW_NATIVE_FULL_SCR_1 = "enable_show_native_full_scr_1";
    public static final String ENABLE_SHOW_NATIVE_FULL_SCR_2 = "enable_show_native_full_scr_2";

    public static final String ENABLE_NOTIFICATION_LOCK_SCR = "enable_notification_lock_scr";
    public static final String ENABLE_NOTIFICATION = "enable_notification";

    public static final String NUMBER_OF_2FLOOR = "number_of_2floor";

    public static final String NUMBER_LOCK_MOVIE = "number_lock_movie";

    public static final String NUMBER_FREE_WATCH_MOVIE = "number_free_watch_movie";
    public static final String SHOW_IAP = "show_iap";

    public static final String LAYOUT_BUTTON_ADS = "layout_button_ads";

    public static final String TIME_SHOW_ICON_X = "nfs_time_x";

    private FirebaseRemoteConfig remoteConfig;
    private GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
    public static boolean canRequestAds = false;

    public ConfigAd configAd;
    public final Map<String, ConfigAdPlaceNative> configMap = new HashMap<>();
    public ConfigUpdate configUpdate;
    public static boolean isChooseLanguage = false;
    public static boolean isFromNotification = false;

    private static FirebaseQuery configController;

    public static FirebaseQuery getConfigController() {
        if (configController == null) {
            configController = new FirebaseQuery();
        }
        return configController;
    }

    private FirebaseQuery() {
    }

    private CallbackTimeout callbackTimeout;

    public void setCallbackTimeout(CallbackTimeout callbackTimeout) {
        this.callbackTimeout = callbackTimeout;
    }

    private CallbackBanner callbackBannerSplash;

    public void setCallbackBannerSplash(CallbackBanner callbackBannerSplash) {
        this.callbackBannerSplash = callbackBannerSplash;
    }

    public void initFirebase(Activity activity, CallbackAd callbackAd) {
        canRequestAds = false;
        remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings
                .Builder()
                .setMinimumFetchIntervalInSeconds(60)
                .setFetchTimeoutInSeconds(30)
                .build();
        remoteConfig.setConfigSettingsAsync(settings);
        remoteConfig.fetchAndActivate()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(activity);
                        googleMobileAdsConsentManager.gatherConsent(
                                activity,
                                consentError -> {
                                    if (consentError != null) {
                                    }
                                    if (googleMobileAdsConsentManager.canRequestAds()) {
                                        if (!canRequestAds) {
                                            queryData(remoteConfig, activity, callbackAd);
                                        }
                                    }
                                }
                        );
                        // This sample attempts to load ads using consent obtained in the previous session.
                        if (googleMobileAdsConsentManager.canRequestAds()) {
                            if (!canRequestAds) {
                                queryData(remoteConfig, activity, callbackAd);
                            }
                        }
                    } else {
                        Exception e = task.getException();
                        if (e instanceof FirebaseRemoteConfigFetchThrottledException) {
                            if (callbackAd != null) {
                                callbackAd.onNextAction();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("TAG", "addOnFailureListener: " + e.getMessage());
                    if (callbackAd != null) {
                        callbackAd.onNextAction();
                    }
                    e.printStackTrace();
                });
    }

    private void queryData(FirebaseRemoteConfig firebaseRemoteConfig, Activity activity, CallbackAd callbackAd) {
        canRequestAds = true;
        try {
            String configAds = firebaseRemoteConfig.getString(CONFIG_ADS);
            Log.e("TAG", "queryData: configAds = " + configAds);
            configAd = new Gson().fromJson(configAds, ConfigAd.class);
            if (configAd != null && configAd.getListAdPlaceNative() != null) {
                for (ConfigAdPlaceNative place : configAd.getListAdPlaceNative()) {
                    configMap.put(place.getName(), place); // Lưu theo name
                }
            }

            String configUpdates = firebaseRemoteConfig.getString(CONFIG_UPDATE);
            Log.e("TAG", "queryData: configUpdate = " + configUpdates);
            configUpdate = new Gson().fromJson(configUpdates, ConfigUpdate.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean isShowOpenStart = firebaseRemoteConfig.getBoolean(IS_SHOW_OPEN_START);
        setIsShowOpenStart(isShowOpenStart);

        boolean enableAds = firebaseRemoteConfig.getBoolean(ENABLE_ADS);
        setEnableAds(enableAds);

        boolean enableOpenStart = firebaseRemoteConfig.getBoolean(ENABLE_OPEN_START);
        setEnableOpenStart(enableOpenStart);

        boolean enableOpenResume = firebaseRemoteConfig.getBoolean(ENABLE_OPEN_RESUME);
        setEnableOpenResume(enableOpenResume);

        boolean enableBanner = firebaseRemoteConfig.getBoolean(ENABLE_BANNER);
        setEnableBanner(enableBanner);

        boolean enableNative = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE);
        setEnableNative(enableNative);

        boolean enableInters = firebaseRemoteConfig.getBoolean(ENABLE_INTERS);
        setEnableInters(enableInters);

        boolean enableReward = firebaseRemoteConfig.getBoolean(ENABLE_REWARD);
        setEnableReward(enableReward);

        boolean enableNativeCollapse = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_COLLAPSE);
        setEnableNativeCollapse(enableNativeCollapse);

        boolean enableBannerCollapse = firebaseRemoteConfig.getBoolean(ENABLE_BANNER_COLLAPSE);
        setEnableBannerCollapse(enableBannerCollapse);

        String idOpenStart = firebaseRemoteConfig.getString(ID_OPEN_START);
        setIdOpenStart(idOpenStart);

        String idOpenResume = firebaseRemoteConfig.getString(ID_OPEN_RESUME);
        setIdOpenResume(idOpenResume);

        String idIntersSplash = firebaseRemoteConfig.getString(ID_INTERS_SPLASH);
        setIdIntersSplash(idIntersSplash);

        String idIntersSplashHigh = firebaseRemoteConfig.getString(ID_INTERS_SPLASH_HIGH);
        setIdIntersSplashHigh(idIntersSplashHigh);

        String idIntersSplashHigh2 = firebaseRemoteConfig.getString(ID_INTERS_SPLASH_2_HIGH);
        setIdIntersSplash2High(idIntersSplashHigh2);

        String idIntersSplashHigh3 = firebaseRemoteConfig.getString(ID_INTERS_SPLASH_3_HIGH);
        setIdIntersSplash3High(idIntersSplashHigh3);

        String idInterInApp = firebaseRemoteConfig.getString(ID_INTERS_IN_APP);
        setIdIntersInApp(idInterInApp);

        String idInterInAppHigh = firebaseRemoteConfig.getString(ID_INTERS_IN_APP_HIGH);
        setIdIntersInAppHigh(idInterInAppHigh);

        String idNativeLanguage = firebaseRemoteConfig.getString(ID_NATIVE_LANGUAGE);
        setIdNativeLanguage(idNativeLanguage);

        String idNativeLanguageHigh = firebaseRemoteConfig.getString(ID_NATIVE_LANGUAGE_HIGH);
        setIdNativeLanguageHigh(idNativeLanguageHigh);

        String idNativeLanguageDup = firebaseRemoteConfig.getString(ID_NATIVE_LANGUAGE_DUP);
        setIdNativeLanguageDup(idNativeLanguageDup);

        String idNativeLanguageDupHigh = firebaseRemoteConfig.getString(ID_NATIVE_LANGUAGE_DUP_HIGH);
        setIdNativeLanguageDupHigh(idNativeLanguageDupHigh);

        String idNativeOnboard1 = firebaseRemoteConfig.getString(ID_NATIVE_ONBOARD_1);
        setIdNativeOnboard1(idNativeOnboard1);

        String idNativeOnboard3 = firebaseRemoteConfig.getString(ID_NATIVE_ONBOARD_3);
        setIdNativeOnboard3(idNativeOnboard3);

        String idNativeOnboard1High = firebaseRemoteConfig.getString(ID_NATIVE_ONBOARD_1_HIGH);
        setIdNativeOnboard1High(idNativeOnboard1High);

        String idNativeOnboard3High = firebaseRemoteConfig.getString(ID_NATIVE_ONBOARD_3_HIGH);
        setIdNativeOnboard3High(idNativeOnboard3High);

        String idNativeFullOnboard1 = firebaseRemoteConfig.getString(ID_NATIVE_FULL_ONBOARD_1);
        setIdNativeFullOnboard1(idNativeFullOnboard1);

        String idNativeFullOnboard2 = firebaseRemoteConfig.getString(ID_NATIVE_FULL_ONBOARD_2);
        setIdNativeFullOnboard2(idNativeFullOnboard2);

        String idNativeFullOnboard1High = firebaseRemoteConfig.getString(ID_NATIVE_FULL_ONBOARD_1_HIGH);
        setIdNativeFullOnboard1High(idNativeFullOnboard1High);

        String idNativeFullOnboard2High = firebaseRemoteConfig.getString(ID_NATIVE_FULL_ONBOARD_2_HIGH);
        setIdNativeFullOnboard2High(idNativeFullOnboard2High);

        String idInterOnboard = firebaseRemoteConfig.getString(ID_INTERS_ONBOARD);
        setIdIntersOnboard(idInterOnboard);

        String idInterOnboardHigh = firebaseRemoteConfig.getString(ID_INTERS_ONBOARD_HIGH);
        setIdIntersOnboardHigh(idInterOnboardHigh);

        String idNativeInApp = firebaseRemoteConfig.getString(ID_NATIVE_IN_APP);
        setIdNativeInApp(idNativeInApp);

        String idNativeMain = firebaseRemoteConfig.getString(ID_NATIVE_MAIN);
        setIdNativeMain(idNativeMain);

        String idBannerSplash = firebaseRemoteConfig.getString(ID_BANNER_SPLASH);
        setIdBannerSplash(idBannerSplash);

        String idBannerInApp = firebaseRemoteConfig.getString(ID_BANNER_IN_APP);
        setIdBannerInApp(idBannerInApp);

        String idRewardInApp = firebaseRemoteConfig.getString(ID_REWARD_IN_APP);
        setIdRewardInApp(idRewardInApp);

        boolean isShowLanguageReopen = firebaseRemoteConfig.getBoolean(IS_SHOW_LANGUAGE_REOPEN);
        setIsShowLanguageReopen(isShowLanguageReopen);

        String timeShowInters = firebaseRemoteConfig.getString(TIME_SHOW_INTERS);
        setTimeShowInters(timeShowInters);

        boolean enableBannerSplash = firebaseRemoteConfig.getBoolean(ENABLE_BANNER_SPLASH);
        setEnableBannerSplash(enableBannerSplash);

        boolean enableIntersSplash = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_SPLASH);
        setEnableIntersSplash(enableIntersSplash);

        boolean enableIntersSplashHigh = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_SPLASH_HIGH);
        setEnableIntersSplashHigh(enableIntersSplashHigh);

        boolean enableNativeLanguage = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_LANGUAGE);
        setEnableNativeLanguage(enableNativeLanguage);

        boolean enableNativeLanguageHigh = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_LANGUAGE_HIGH);
        setEnableNativeLanguageHigh(enableNativeLanguageHigh);

        boolean enableNativeLanguageDup = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_LANGUAGE_DUP);
        setEnableNativeLanguageDup(enableNativeLanguageDup);

        boolean enableNativeLanguageDupHigh = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_LANGUAGE_DUP_HIGH);
        setEnableNativeLanguageDupHigh(enableNativeLanguageDupHigh);

        boolean enableNativeOnboard1 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_ONBOARD_1);
        setEnableNativeOnboard1(enableNativeOnboard1);

        boolean enableNativeOnboard2 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_ONBOARD_3);
        setEnableNativeOnboard3(enableNativeOnboard2);

        boolean enableNativeOnboardHigh1 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_ONBOARD_1_HIGH);
        setEnableNativeOnboard1High(enableNativeOnboardHigh1);

        boolean enableNativeOnboardHigh2 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_ONBOARD_3_HIGH);
        setEnableNativeOnboard3High(enableNativeOnboardHigh2);

        boolean enableNativeFullScr1 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_FULL_SCR_1);
        setEnableNativeFullScr1(enableNativeFullScr1);

        boolean enableNativeFullScr2 = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_FULL_SCR_2);
        setEnableNativeFullScr2(enableNativeFullScr2);

        boolean enableNativeFullScr1High = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_FULL_SCR_1_HIGH);
        setEnableNativeFullScr1High(enableNativeFullScr1High);

        boolean enableNativeFullScr2High = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_FULL_SCR_2_HIGH);
        setEnableNativeFullScr2High(enableNativeFullScr2High);

        boolean enableIntersOnboard = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_ONBOARD);
        setEnableIntersOnboard(enableIntersOnboard);

        boolean enableIntersOnboardHigh = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_ONBOARD_HIGH);
        setEnableIntersOnboardHigh(enableIntersOnboardHigh);

        boolean enableIntersInApp = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_IN_APP);
        setEnableIntersInApp(enableIntersInApp);

        boolean enableIntersInAppHigh = firebaseRemoteConfig.getBoolean(ENABLE_INTERS_IN_APP_HIGH);
        setEnableIntersInAppHigh(enableIntersInAppHigh);

        boolean enableIap = firebaseRemoteConfig.getBoolean(SHOW_IAP);
        setEnableIap(enableIap);

        long timeScrollNativeFullScr = firebaseRemoteConfig.getLong(TIME_SCROLL_NATIVE_FULL_SCR);
        setTimeScrollNativeFullScr(timeScrollNativeFullScr);

        boolean enableNativeInApp = firebaseRemoteConfig.getBoolean(ENABLE_NATIVE_IN_APP);
        setEnableNativeInApp(enableNativeInApp);

        boolean enableShowNativeFullScr1 = firebaseRemoteConfig.getBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_1);
        setEnableShowNativeFullScr1(enableShowNativeFullScr1);

        boolean enableShowNativeFullScr2 = firebaseRemoteConfig.getBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_2);
        setEnableShowNativeFullScr2(enableShowNativeFullScr2);

        long numberOf2Floor = firebaseRemoteConfig.getLong(NUMBER_OF_2FLOOR);
        setNumberOf2Floor(numberOf2Floor);

        long numberLockMovie = firebaseRemoteConfig.getLong(NUMBER_LOCK_MOVIE);
        setNumberLockMovie(numberLockMovie);

        long numberFreeWatchMovie = firebaseRemoteConfig.getLong(NUMBER_FREE_WATCH_MOVIE);
        setNumberFreeWatchMovie(numberFreeWatchMovie);

        long timeShowIconX = firebaseRemoteConfig.getLong(TIME_SHOW_ICON_X);
        setTimeShowIconX(timeShowIconX);

        String layoutButtonAds = firebaseRemoteConfig.getString(LAYOUT_BUTTON_ADS);
        setLayoutButtonAds(layoutButtonAds);
        if (enableAds && enableBannerSplash) {
            BannerSplash.getInstance().loadAds(activity, callbackBannerSplash);
        } else {
            if (callbackBannerSplash != null) {
                callbackBannerSplash.onFailed();
            }
        }

        if (!PurchaseUtils.isNoAds(activity)) {
            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                    if (callbackTimeout != null) {
                        callbackTimeout.cancelTimeout();
                    }
                    if (enableAds) {
                        if (isShowLanguageReopen) {
                            NativeLanguageAll.getInstance().loadAdsAll(activity);
                        } else {
                            if (!isChooseLanguage) {
                                NativeLanguageAll.getInstance().loadAdsAll(activity);
                            }
                        }

                        if (isShowOpenStart) {
                            OpenAdsManager.getOpenAds().showOpenAds(activity, callbackAd);
                        } else {
                            IntersSplashAll.getInstance().loadAnsShow(activity, callbackAd);
                        }
                    } else {
                        if (callbackAd != null) {
                            callbackAd.onNextAction();
                        }
                    }
                }
            });
            MobileAds.openAdInspector(activity, adInspectorError -> {
                if (adInspectorError != null) {
                    Log.e("AdInspector", "Lỗi khi mở Ad Inspector: ${error.message}");
                } else {
                    Log.d("AdInspector", "Ad Inspector đã được mở");
                }
            });
        } else {
            if (callbackAd != null) {
                callbackAd.onNextAction();
            }
        }

        boolean enableNotificationLockScr = firebaseRemoteConfig.getBoolean(ENABLE_NOTIFICATION_LOCK_SCR);
        setEnableNotificationLockScr(enableNotificationLockScr);

        boolean enableNotification = firebaseRemoteConfig.getBoolean(ENABLE_NOTIFICATION);
        setEnableNotification(enableNotification);
    }

    public static void setIsShowOpenStart(boolean enable) {
        SharePreferUtils.putBoolean(IS_SHOW_OPEN_START, enable);
    }

    public static boolean getIsShowOpenStart() {
        return SharePreferUtils.getBoolean(IS_SHOW_OPEN_START, false);
    }

    public static void setEnableAds(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_ADS, enable);
    }

    public static boolean getEnableAds() {
        return SharePreferUtils.getBoolean(ENABLE_ADS, true);
    }

    public static void setEnableOpenResume(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_OPEN_RESUME, enable);
    }

    public static boolean getEnableOpenResume() {
        return SharePreferUtils.getBoolean(ENABLE_OPEN_RESUME, true);
    }

    public static void setEnableOpenStart(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_OPEN_START, enable);
    }

    public static boolean getEnableOpenStart() {
        return SharePreferUtils.getBoolean(ENABLE_OPEN_START, true);
    }

    public static void setEnableBannerCollapse(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_BANNER_COLLAPSE, enable);
    }

    public static boolean getEnableBannerCollapse() {
        return SharePreferUtils.getBoolean(ENABLE_BANNER_COLLAPSE, true);
    }

    public static void setEnableBanner(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_BANNER, enable);
    }

    public static boolean getEnableBanner() {
        return SharePreferUtils.getBoolean(ENABLE_BANNER, true);
    }

    public static void setEnableNative(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE, enable);
    }

    public static boolean getEnableNative() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE, true);
    }

    public static void setEnableInters(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS, enable);
    }

    public static boolean getEnableInters() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS, true);
    }

    public static void setEnableReward(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_REWARD, enable);
    }

    public static boolean getEnableReward() {
        return SharePreferUtils.getBoolean(ENABLE_REWARD, true);
    }

    public static void setEnableNativeCollapse(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_COLLAPSE, enable);
    }

    public static boolean getEnableNativeCollapse() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_COLLAPSE, true);
    }

    public static void setTimeShowInters(String timeShow) {
        SharePreferUtils.putString(TIME_SHOW_INTERS, timeShow);
    }

    public static String getTimeShowInters() {
        return SharePreferUtils.getString(TIME_SHOW_INTERS, "15000");
    }

    public static void setIdIntersSplash(String idAds) {
        SharePreferUtils.putString(ID_INTERS_SPLASH, idAds);
    }

    public static String getIdIntersSplash() {
        return SharePreferUtils.getString(ID_INTERS_SPLASH, "");
    }

    public static void setIdIntersSplashHigh(String idAds) {
        SharePreferUtils.putString(ID_INTERS_SPLASH_HIGH, idAds);
    }

    public static String getIdIntersSplashHigh() {
        return SharePreferUtils.getString(ID_INTERS_SPLASH_HIGH, "");
    }

    public static void setIdIntersSplash2High(String idAds) {
        SharePreferUtils.putString(ID_INTERS_SPLASH_2_HIGH, idAds);
    }

    public static String getIdIntersSplash2High() {
        return SharePreferUtils.getString(ID_INTERS_SPLASH_2_HIGH, "");
    }

    public static void setIdIntersSplash3High(String idAds) {
        SharePreferUtils.putString(ID_INTERS_SPLASH_3_HIGH, idAds);
    }

    public static String getIdIntersSplash3High() {
        return SharePreferUtils.getString(ID_INTERS_SPLASH_3_HIGH, "");
    }

    public static void setIdIntersInApp(String idAds) {
        SharePreferUtils.putString(ID_INTERS_IN_APP, idAds);
    }

    public static String getIdIntersInApp() {
        return SharePreferUtils.getString(ID_INTERS_IN_APP, "");
    }

    public static void setIdIntersInAppHigh(String idAds) {
        SharePreferUtils.putString(ID_INTERS_IN_APP_HIGH, idAds);
    }

    public static String getIdIntersInAppHigh() {
        return SharePreferUtils.getString(ID_INTERS_IN_APP_HIGH, "");
    }

    public static void setIdNativeLanguage(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_LANGUAGE, idAds);
    }

    public static String getIdNativeLanguage() {
        return SharePreferUtils.getString(ID_NATIVE_LANGUAGE, "");
    }

    public static void setIdNativeLanguageHigh(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_LANGUAGE_HIGH, idAds);
    }

    public static String getIdNativeLanguageHigh() {
        return SharePreferUtils.getString(ID_NATIVE_LANGUAGE_HIGH, "");
    }

    public static void setIdNativeLanguageDup(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_LANGUAGE_DUP, idAds);
    }

    public static String getIdNativeLanguageDup() {
        return SharePreferUtils.getString(ID_NATIVE_LANGUAGE_DUP, "");
    }

    public static void setIdNativeLanguageDupHigh(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_LANGUAGE_DUP_HIGH, idAds);
    }

    public static String getIdNativeLanguageDupHigh() {
        return SharePreferUtils.getString(ID_NATIVE_LANGUAGE_DUP_HIGH, "");
    }

    public static void setIdNativeOnboard1(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_ONBOARD_1, idAds);
    }

    public static String getIdNativeOnboard1() {
        return SharePreferUtils.getString(ID_NATIVE_ONBOARD_1, "");
    }

    public static void setIdNativeOnboard3(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_ONBOARD_3, idAds);
    }

    public static String getIdNativeOnboard3() {
        return SharePreferUtils.getString(ID_NATIVE_ONBOARD_3, "");
    }

    public static void setIdNativeOnboard1High(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_ONBOARD_1_HIGH, idAds);
    }

    public static String getIdNativeOnboard1High() {
        return SharePreferUtils.getString(ID_NATIVE_ONBOARD_1_HIGH, "");
    }

    public static void setIdNativeOnboard3High(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_ONBOARD_3_HIGH, idAds);
    }

    public static String getIdNativeOnboard3High() {
        return SharePreferUtils.getString(ID_NATIVE_ONBOARD_3_HIGH, "");
    }

    public static void setIdNativeFullOnboard1(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_FULL_ONBOARD_1, idAds);
    }

    public static String getIdNativeFullOnboard1() {
        return SharePreferUtils.getString(ID_NATIVE_FULL_ONBOARD_1, "");
    }

    public static void setIdNativeFullOnboard2(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_FULL_ONBOARD_2, idAds);
    }

    public static String getIdNativeFullOnboard2() {
        return SharePreferUtils.getString(ID_NATIVE_FULL_ONBOARD_2, "");
    }

    public static void setIdNativeFullOnboard1High(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_FULL_ONBOARD_1_HIGH, idAds);
    }

    public static String getIdNativeFullOnboard1High() {
        return SharePreferUtils.getString(ID_NATIVE_FULL_ONBOARD_1_HIGH, "");
    }

    public static void setIdNativeFullOnboard2High(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_FULL_ONBOARD_2_HIGH, idAds);
    }

    public static String getIdNativeFullOnboard2High() {
        return SharePreferUtils.getString(ID_NATIVE_FULL_ONBOARD_2_HIGH, "");
    }

    public static void setIdIntersOnboard(String idAds) {
        SharePreferUtils.putString(ID_INTERS_ONBOARD, idAds);
    }

    public static String getIdIntersOnboard() {
        return SharePreferUtils.getString(ID_INTERS_ONBOARD, "");
    }

    public static void setIdIntersOnboardHigh(String idAds) {
        SharePreferUtils.putString(ID_INTERS_ONBOARD_HIGH, idAds);
    }

    public static String getIdIntersOnboardHigh() {
        return SharePreferUtils.getString(ID_INTERS_ONBOARD_HIGH, "");
    }

    public static void setIdNativeInApp(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_IN_APP, idAds);
    }

    public static String getIdNativeInApp() {
        return SharePreferUtils.getString(ID_NATIVE_IN_APP, "");
    }

    public static void setIdNativeMain(String idAds) {
        SharePreferUtils.putString(ID_NATIVE_MAIN, idAds);
    }

    public static String getIdNativeMain() {
        return SharePreferUtils.getString(ID_NATIVE_MAIN, "");
    }

    public static void setIdOpenStart(String idAds) {
        SharePreferUtils.putString(ID_OPEN_START, idAds);
    }

    public static String getIdOpenStart() {
        return SharePreferUtils.getString(ID_OPEN_START, "");
    }

    public static void setIdOpenResume(String idAds) {
        SharePreferUtils.putString(ID_OPEN_RESUME, idAds);
    }

    public static String getIdOpenResume() {
        return SharePreferUtils.getString(ID_OPEN_RESUME, "");
    }

    public static void setIdBannerSplash(String idAds) {
        SharePreferUtils.putString(ID_BANNER_SPLASH, idAds);
    }

    public static String getIdBannerSplash() {
        return SharePreferUtils.getString(ID_BANNER_SPLASH, "");
    }

    public static void setIdBannerInApp(String idAds) {
        SharePreferUtils.putString(ID_BANNER_IN_APP, idAds);
    }

    public static String getIdBannerInApp() {
        return SharePreferUtils.getString(ID_BANNER_IN_APP, "");
    }

    public static void setIdRewardInApp(String idAds) {
        SharePreferUtils.putString(ID_REWARD_IN_APP, idAds);
    }

    public static String getIdRewardInApp() {
        return SharePreferUtils.getString(ID_REWARD_IN_APP, "");
    }

    public static void setIsShowLanguageReopen(boolean enable) {
        SharePreferUtils.putBoolean(IS_SHOW_LANGUAGE_REOPEN, enable);
    }

    public static boolean getIsShowLanguageReopen() {
        return SharePreferUtils.getBoolean(IS_SHOW_LANGUAGE_REOPEN, false);
    }

    public static void setEnableBannerSplash(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_BANNER_SPLASH, enable);
    }

    public static boolean getEnableBannerSplash() {
        return SharePreferUtils.getBoolean(ENABLE_BANNER_SPLASH, true);
    }

    public static void setEnableIntersSplash(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_SPLASH, enable);
    }

    public static boolean getEnableIntersSplash() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_SPLASH, true);
    }

    public static void setEnableIntersSplashHigh(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_SPLASH_HIGH, enable);
    }

    public static boolean getEnableIntersSplashHigh() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_SPLASH_HIGH, true);
    }

    public static void setEnableNativeLanguage(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_LANGUAGE, enable);
    }

    public static boolean getEnableNativeLanguage() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_LANGUAGE, true);
    }

    public static void setEnableNativeLanguageHigh(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_LANGUAGE_HIGH, enable);
    }

    public static boolean getEnableNativeLanguageHigh() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_LANGUAGE_HIGH, true);
    }

    public static void setEnableNativeLanguageDup(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_LANGUAGE_DUP, enable);
    }

    public static boolean getEnableNativeLanguageDup() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_LANGUAGE_DUP, true);
    }

    public static void setEnableNativeLanguageDupHigh(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_LANGUAGE_DUP_HIGH, enable);
    }

    public static boolean getEnableNativeLanguageDupHigh() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_LANGUAGE_DUP_HIGH, true);
    }

    public static void setEnableNativeOnboard1(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_ONBOARD_1, enable);
    }

    public static boolean getEnableNativeOnboard1() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_ONBOARD_1, true);
    }

    public static void setEnableNativeOnboard3(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_ONBOARD_3, enable);
    }

    public static boolean getEnableNativeOnboard3() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_ONBOARD_3, true);
    }

    public static void setEnableNativeOnboard1High(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_ONBOARD_1_HIGH, enable);
    }

    public static boolean getEnableNativeOnboard1High() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_ONBOARD_1_HIGH, true);
    }

    public static void setEnableNativeOnboard3High(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_ONBOARD_3_HIGH, enable);
    }

    public static boolean getEnableNativeOnboard3High() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_ONBOARD_3_HIGH, true);
    }

    public static void setEnableNativeFullScr1(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_FULL_SCR_1, enable);
    }

    public static boolean getEnableNativeFullScr1() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_FULL_SCR_1, true);
    }

    public static void setEnableNativeFullScr2(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_FULL_SCR_2, enable);
    }

    public static boolean getEnableNativeFullScr2() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_FULL_SCR_2, true);
    }

    public static void setEnableNativeFullScr1High(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_FULL_SCR_1_HIGH, enable);
    }

    public static boolean getEnableNativeFullScr1High() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_FULL_SCR_1_HIGH, true);
    }

    public static void setEnableNativeFullScr2High(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_FULL_SCR_2_HIGH, enable);
    }

    public static boolean getEnableNativeFullScr2High() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_FULL_SCR_2_HIGH, true);
    }

    public static void setEnableShowNativeFullScr1(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_1, enable);
    }

    public static boolean getEnableShowNativeFullScr1() {
        return SharePreferUtils.getBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_1, true);
    }

    public static void setEnableShowNativeFullScr2(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_2, enable);
    }

    public static boolean getEnableShowNativeFullScr2() {
        return SharePreferUtils.getBoolean(ENABLE_SHOW_NATIVE_FULL_SCR_2, true);
    }

    public static void setEnableIntersInApp(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_IN_APP, enable);
    }

    public static boolean getEnableIntersInApp() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_IN_APP, true);
    }

    public static void setEnableIntersOnboard(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_ONBOARD, enable);
    }

    public static boolean getEnableIntersOnboard() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_ONBOARD, true);
    }

    public static void setEnableIntersOnboardHigh(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_ONBOARD_HIGH, enable);
    }

    public static boolean getEnableIntersOnboardHigh() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_ONBOARD_HIGH, true);
    }

    public static void setEnableIntersInAppHigh(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_INTERS_IN_APP_HIGH, enable);
    }

    public static boolean getEnableIntersInAppHigh() {
        return SharePreferUtils.getBoolean(ENABLE_INTERS_IN_APP_HIGH, true);
    }

    public static void setEnableIap(boolean enable) {
        SharePreferUtils.putBoolean(SHOW_IAP, enable);
    }

    public static boolean getEnableIap() {
        return SharePreferUtils.getBoolean(SHOW_IAP, true);
    }

    public static void setTimeScrollNativeFullScr(long timeScroll) {
        SharePreferUtils.putLong(TIME_SCROLL_NATIVE_FULL_SCR, timeScroll);
    }

    public static long getTimeScrollNativeFullScr() {
        return SharePreferUtils.getLong(TIME_SCROLL_NATIVE_FULL_SCR, 15000);
    }

    public static void setEnableNativeInApp(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NATIVE_IN_APP, enable);
    }

    public static boolean getEnableNativeInApp() {
        return SharePreferUtils.getBoolean(ENABLE_NATIVE_IN_APP, true);
    }

    public static void setEnableNotificationLockScr(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NOTIFICATION_LOCK_SCR, enable);
    }

    public static boolean getEnableNotificationLockScr() {
        return SharePreferUtils.getBoolean(ENABLE_NOTIFICATION_LOCK_SCR, true);
    }

    public static void setEnableNotification(boolean enable) {
        SharePreferUtils.putBoolean(ENABLE_NOTIFICATION, enable);
    }

    public static boolean getEnableNotification() {
        return SharePreferUtils.getBoolean(ENABLE_NOTIFICATION, true);
    }

    public static void setNumberOf2Floor(long timeScroll) {
        SharePreferUtils.putLong(NUMBER_OF_2FLOOR, timeScroll);
    }

    public static long getNumberOf2Floor() {
        return SharePreferUtils.getLong(NUMBER_OF_2FLOOR, 2);
    }

    public static void setNumberLockMovie(long timeScroll) {
        SharePreferUtils.putLong(NUMBER_LOCK_MOVIE, timeScroll);
    }

    public static long getNumberLockMovie() {
        return SharePreferUtils.getLong(NUMBER_LOCK_MOVIE, 5);
    }

    public static void setNumberFreeWatchMovie(long timeScroll) {
        SharePreferUtils.putLong(NUMBER_FREE_WATCH_MOVIE, timeScroll);
    }

    public static long getNumberFreeWatchMovie() {
        return SharePreferUtils.getLong(NUMBER_FREE_WATCH_MOVIE, 5);
    }
    public static void setLayoutButtonAds(String layout) {
        SharePreferUtils.putString(LAYOUT_BUTTON_ADS, layout);
    }

    public static String getLayoutButtonAds() {
        return SharePreferUtils.getString(LAYOUT_BUTTON_ADS, "");
    }

    public static void setTimeShowIconX(long timeScroll) {
        SharePreferUtils.putLong(TIME_SHOW_ICON_X, timeScroll);
    }

    public static long getTimeShowIconX() {
        return SharePreferUtils.getLong(TIME_SHOW_ICON_X, 5000L);
    }

}