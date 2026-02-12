package com.module.ads.admob.inters;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.module.ads.callback.CallbackAd;
import com.module.ads.mmp.AdjustTracking;
import com.module.ads.remote.FirebaseQuery;
import com.module.ads.utils.DialogUtils;
import com.module.ads.utils.FBTracking;
import com.module.ads.utils.HomeUtils;
import com.module.ads.utils.PurchaseUtils;


public class IntersOnboard {

    public CallbackAd mCallbackAd;
    public static InterstitialAd interstitialAdHigh;
    public static InterstitialAd interstitialAdNormal;
    public boolean isShowing = false;
    private long timeLoad = 0;
    private boolean isLoadingHigh = false;
    private boolean isLoadingNormal = false;

    private static IntersOnboard mIntersInApp;

    public static IntersOnboard getInstance() {
        if (mIntersInApp == null) {
            mIntersInApp = new IntersOnboard();
        }
        return mIntersInApp;
    }

    private void loadAdsHigh(final Activity activity) {
        if (interstitialAdHigh != null || isLoadingHigh) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters()) {
            if (FirebaseQuery.getEnableIntersOnboardHigh()) {
                isLoadingHigh = true;
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, FirebaseQuery.getIdIntersOnboardHigh(), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                Log.e("TamBT", "onAdLoaded: inter onboard high");
                                isLoadingHigh = false;
                                interstitialAdHigh = interstitialAd;
                                interstitialAd.setOnPaidEventListener(adValue -> {
                                    double revenue = adValue.getValueMicros() / 1000000.0;
                                    AdapterResponseInfo loadedAdapterResponseInfo = interstitialAd.getResponseInfo().getLoadedAdapterResponseInfo();
                                    AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
                                    FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Interstitial");
                                });
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        HomeUtils.setHomeClick(false);
                                        isShowing = false;
                                        isLoadingHigh = false;
                                        isLoadingNormal = false;
                                        interstitialAdHigh = null;
                                        loadAdsAll(activity);
                                        IntersInApp.getInstance().timeLoad = System.currentTimeMillis();
                                        DialogUtils.dismissDialogLoading();
                                        if (mCallbackAd != null) {
                                            mCallbackAd.onNextAction();
                                        }
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        super.onAdShowedFullScreenContent();
                                        isShowing = true;
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.e("TamBT", "onAdFailedToLoad: inter onboard high" + loadAdError.getMessage());
                                isLoadingHigh = false;
                                interstitialAdHigh = null;
                                if (FirebaseQuery.getEnableIntersOnboard()) {
                                    loadAdsNormal(activity);
                                }
                            }
                        });
            } else {
                isLoadingHigh = false;
                if (FirebaseQuery.getEnableIntersOnboard()) {
                    loadAdsNormal(activity);
                }
            }
        }
    }

    private void loadAdsNormal(final Activity activity) {
        if (interstitialAdNormal != null || isLoadingNormal) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters() && FirebaseQuery.getEnableIntersOnboard()) {
            isLoadingNormal = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, FirebaseQuery.getIdIntersOnboard(), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            Log.e("TamBT", "onAdLoaded: inter onboard normal");
                            isLoadingNormal = false;
                            interstitialAdNormal = interstitialAd;
                            interstitialAd.setOnPaidEventListener(new OnPaidEventListener() {
                                @Override
                                public void onPaidEvent(@NonNull AdValue adValue) {
                                    double revenue = adValue.getValueMicros() / 1000000.0;
                                    AdapterResponseInfo loadedAdapterResponseInfo = interstitialAd.getResponseInfo().getLoadedAdapterResponseInfo();
                                    AdjustTracking.adjustTrackingRev(revenue, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
                                    FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), activity.getClass(), "Interstitial");
                                }
                            });
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    isLoadingHigh = false;
                                    isLoadingNormal = false;
                                    interstitialAdHigh = null;
                                    interstitialAdNormal = null;
                                    loadAdsAll(activity);
                                    IntersInApp.getInstance().timeLoad = System.currentTimeMillis();
                                    DialogUtils.dismissDialogLoading();
                                    if (mCallbackAd != null) {
                                        mCallbackAd.onNextAction();
                                    }
                                }

                                @Override
                                public void onAdImpression() {
                                    FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    isShowing = true;
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.e("TamBT", "onAdFailedToLoad: inter onboard normal" + loadAdError.getMessage());
                            isLoadingHigh = false;
                            isLoadingNormal = false;
                            interstitialAdHigh = null;
                            interstitialAdNormal = null;
                        }
                    });

        }
    }

    public void loadAdsAll(Activity activity) {
        if (FirebaseQuery.getEnableIntersOnboardHigh()) {
            loadAdsHigh(activity);
        } else if (FirebaseQuery.getEnableIntersOnboard()) {
            loadAdsNormal(activity);
        }
    }

    public void showAds(Activity activity, CallbackAd callbackAd) {
        this.mCallbackAd = callbackAd;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters()) {
            InterstitialAd interstitialAd;
            if (interstitialAdHigh != null) {
                interstitialAd = interstitialAdHigh;
            } else if (interstitialAdNormal != null) {
                interstitialAd = interstitialAdNormal;
            } else {
                interstitialAd = null;
            }
            if (interstitialAd != null) {
                DialogUtils.showDialogLoading(activity);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (interstitialAd != null) {
                            interstitialAd.show(activity);
                        }
                    }
                }, 500L);
            } else {
                loadAdsAll(activity);
                if (callbackAd != null) {
                    callbackAd.onNextAction();
                }
            }
        } else {
            if (callbackAd != null) {
                callbackAd.onNextAction();
            }
        }
    }
}
