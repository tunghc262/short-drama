package com.module.ads.utils;

import android.app.Activity;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.module.ads.mmp.AdjustTracking;

public class AdUtils {
    public static void onPaidEvent(Activity activity, InterstitialAd interstitialAd) {
        interstitialAd.setOnPaidEventListener(adValue -> {
            double revenue = adValue.getValueMicros() / 1000000.0;
            AdapterResponseInfo loadedAdapterResponseInfo = interstitialAd.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Interstitial");
        });
    }

    public static void onPaidEvent(Activity activity, NativeAd nativeAd) {
        nativeAd.setOnPaidEventListener(adValue -> {
            double revenue = adValue.getValueMicros() / 1000000.0;
            AdapterResponseInfo loadedAdapterResponseInfo = nativeAd.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Native");
        });
    }

    public static void onPaidEvent(Activity activity, AdView adView) {
        adView.setOnPaidEventListener(adValue -> {
            double revenue = adValue.getValueMicros() / 1000000.0;
            AdapterResponseInfo loadedAdapterResponseInfo = adView.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Banner");
        });
    }

    public static void onPaidEvent(Activity activity, RewardedAd rewardedAd) {
        rewardedAd.setOnPaidEventListener(adValue -> {
            double revenue = adValue.getValueMicros() / 1000000.0;
            AdapterResponseInfo loadedAdapterResponseInfo = rewardedAd.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Reward");
        });
    }

    public static void onPaidEvent(Activity activity, AppOpenAd appOpenAd) {
        appOpenAd.setOnPaidEventListener(adValue -> {
            double revenue = adValue.getValueMicros() / 1000000.0;
            AdapterResponseInfo loadedAdapterResponseInfo = appOpenAd.getResponseInfo().getLoadedAdapterResponseInfo();
            AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "OpenAds");
        });
    }
}
