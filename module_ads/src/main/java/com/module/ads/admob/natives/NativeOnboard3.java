package com.module.ads.admob.natives;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.module.ads.callback.CallbackNative;
import com.module.ads.mmp.AdjustTracking;
import com.module.ads.remote.FirebaseQuery;
import com.module.ads.utils.AdUtils;
import com.module.ads.utils.FBTracking;
import com.module.ads.utils.PurchaseUtils;


public class NativeOnboard3 {

    private static NativeOnboard3 nativeOnBoard;
    public static NativeAd nativeAdHigh;
    public static NativeAd nativeAdNormal;
    private boolean isLoadingHigh = false;
    private boolean isLoadingNormal = false;

    public static NativeOnboard3 getInstance() {
        if (nativeOnBoard == null) {
            nativeOnBoard = new NativeOnboard3();
        }
        return nativeOnBoard;
    }

    private CallbackNative callbackNative;

    public void setCallbackNative(CallbackNative callbackNative) {
        this.callbackNative = callbackNative;
    }

    private void loadAdsHigh(final Activity activity) {
        try {
            if (isLoadingHigh || nativeAdHigh != null) return;
            if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableNative()) {
                if (FirebaseQuery.getEnableNativeOnboard3High()) {
                    isLoadingHigh = true;
                    AdLoader.Builder builder = new AdLoader.Builder(activity, FirebaseQuery.getIdNativeOnboard3High());
                    builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            isLoadingHigh = false;
                            NativeOnboard3.nativeAdHigh = nativeAd;
                            AdUtils.onPaidEvent(activity, nativeAd);
                        }
                    });

                    AdLoader adLoader = builder.withAdListener(new AdListener() {

                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            Log.e("TAG", "onAdLoaded: native onboard high");
                            isLoadingHigh = false;
                            if (callbackNative != null) {
                                callbackNative.onLoaded();
                            }
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.e("TAG", "onAdFailedToLoad: native onboard high");
                            isLoadingHigh = false;
                            if (FirebaseQuery.getEnableNativeOnboard3()) {
                                loadAdsNormal(activity);
                            } else {
                                if (callbackNative != null) {
                                    callbackNative.onFailed();
                                }
                            }
                        }

                        @Override
                        public void onAdImpression() {
                            FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                            if (callbackNative != null) {
                                callbackNative.onAdImpression();
                            }
                        }
                    }).build();

                    VideoOptions videoOptions = new VideoOptions.Builder().build();
                    NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
                    builder.withNativeAdOptions(adOptions);
                    adLoader.loadAd(new AdRequest.Builder().build());
                } else {
                    isLoadingHigh = false;
                    if (FirebaseQuery.getEnableNativeOnboard3()) {
                        loadAdsNormal(activity);
                    } else {
                        if (callbackNative != null) {
                            callbackNative.onFailed();
                        }
                    }
                }
            } else {
                isLoadingHigh = false;
                if (callbackNative != null) {
                    callbackNative.onFailed();
                }
            }
        } catch (Exception e) {
            isLoadingHigh = false;
            e.printStackTrace();
        }
    }

    private void loadAdsNormal(final Activity activity) {
        try {
            if (isLoadingNormal || nativeAdNormal != null) return;
            if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableNative() && FirebaseQuery.getEnableNativeOnboard3()) {
                isLoadingNormal = true;
                AdLoader.Builder builder = new AdLoader.Builder(activity, FirebaseQuery.getIdNativeOnboard3());
                builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                        isLoadingNormal = false;
                        nativeAdNormal = nativeAd;
                        AdUtils.onPaidEvent(activity, nativeAd);
                    }
                });

                AdLoader adLoader = builder.withAdListener(new AdListener() {

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        Log.e("TAG", "onAdLoaded: native onboard normal");
                        isLoadingNormal = false;
                        if (callbackNative != null) {
                            callbackNative.onLoaded();
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e("TAG", "onAdFailedToLoad: native onboard normal");
                        isLoadingNormal = false;
                        if (callbackNative != null) {
                            callbackNative.onFailed();
                        }
                    }

                    @Override
                    public void onAdImpression() {
                        FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                        if (callbackNative != null) {
                            callbackNative.onAdImpression();
                        }
                    }
                }).build();

                VideoOptions videoOptions = new VideoOptions.Builder().build();
                NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
                builder.withNativeAdOptions(adOptions);
                adLoader.loadAd(new AdRequest.Builder().build());
            } else {
                isLoadingNormal = false;
                if (callbackNative != null) {
                    callbackNative.onFailed();
                }
            }
        } catch (Exception e) {
            isLoadingNormal = false;
            e.printStackTrace();
        }
    }

    public void loadAdsAll(Activity activity) {
        if (FirebaseQuery.getEnableNativeOnboard3High()) {
            loadAdsHigh(activity);
        } else if (FirebaseQuery.getEnableNativeOnboard3()) {
            loadAdsNormal(activity);
        } else {
            if (callbackNative != null) {
                callbackNative.onFailed();
            }
        }
    }

    public void showAdsAll(Activity activity, LinearLayout lnNative, String namePlace) {
        try {
            NativeAd nativeAd = null;
            if (nativeAdHigh != null) {
                nativeAd = nativeAdHigh;
            } else if (nativeAdNormal != null) {
                nativeAd = nativeAdNormal;
            }
            if (nativeAd != null) {
                lnNative.setVisibility(View.VISIBLE);
                NativeAdView nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(NativeUtils.getLayoutNative(namePlace), null);
                NativeUtils.populateNativeAdView(nativeAd, nativeAdView, namePlace);
                lnNative.removeAllViews();
                lnNative.addView(nativeAdView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroyNativeAd() {
        nativeAdHigh = null;
        nativeAdNormal = null;
        callbackNative = null;
    }
}
