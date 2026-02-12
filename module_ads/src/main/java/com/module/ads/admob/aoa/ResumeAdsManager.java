package com.module.ads.admob.aoa;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.AdapterResponseInfo;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.module.ads.mmp.AdjustTracking;
import com.module.ads.remote.FirebaseQuery;
import com.module.ads.utils.DialogUtils;
import com.module.ads.utils.FBTracking;
import com.module.ads.utils.PurchaseUtils;

import java.util.Date;

public class ResumeAdsManager {
    private static final String TAG = "ResumeAdsManager";
    public AppOpenAd appOpenAd = null;
    public boolean isLoadingAd = false;
    public boolean isShowingAd = false;

    private long loadTime = 0;

    public static boolean isAdFullShowing = false;

    private static ResumeAdsManager resumeAdsManager;
    public static boolean shouldReloadAd = false;

    public static boolean isEnableShowAds = true;

    public static ResumeAdsManager getOpenAds() {
        if (resumeAdsManager == null) {
            resumeAdsManager = new ResumeAdsManager();
        }
        return resumeAdsManager;
    }

    public interface AppOpenListener {
        void onAdLoaded();

        void onAdFailedToLoad();
    }

    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }

    public void loadAd(Context context, AppOpenListener listener) {
        Log.e("ResumeAdsManager", "loadAd: 1");
        if (isLoadingAd || isAdAvailable() || PurchaseUtils.isNoAds(context) || !FirebaseQuery.getEnableAds() || !FirebaseQuery.getEnableOpenResume()) {
            return;
        }
        isLoadingAd = true;
        Log.e("ResumeAdsManager", "loadAd: 2");
        AdManagerAdRequest request = new AdManagerAdRequest.Builder().build();
        AppOpenAd.load(
                context,
                FirebaseQuery.getIdOpenResume(),
                request,
                new AppOpenAd.AppOpenAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AppOpenAd ad) {
                        Log.e("ResumeAdsManager", "onAdLoaded: open resume");
                        appOpenAd = ad;
                        isLoadingAd = false;
                        loadTime = new Date().getTime();

                        appOpenAd.setOnPaidEventListener(new OnPaidEventListener() {
                            @Override
                            public void onPaidEvent(@NonNull AdValue adValue) {
                                long revenue = adValue.getValueMicros() / 1000000;
                                AdapterResponseInfo loadedAdapterResponseInfo = appOpenAd.getResponseInfo().getLoadedAdapterResponseInfo();
                                AdjustTracking.adjustTrackingRev(adValue.getValueMicros() / 1000000.0, adValue.getCurrencyCode(), loadedAdapterResponseInfo.getAdSourceName());
                                FBTracking.funcTrackingIAA(context, FBTracking.EVENT_AD_IMPRESSION, String.valueOf(revenue), context.getClass(), "ResumeAds");
                            }
                        });
                        if (listener != null) {
                            listener.onAdLoaded();
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.e(TAG, "onAdFailedToLoad: open resume: " + loadAdError.getMessage());
                        isLoadingAd = false;
                        if (listener != null) {
                            listener.onAdFailedToLoad();
                        }
                    }
                }
        );
    }

    public void loadAd(Context context) {
        loadAd(context, null);
    }

    // ================================
    // Check if ad is fresh
    // ================================
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = new Date().getTime() - loadTime;
        long numMilliSecondsPerHour = 3600000;
        return dateDifference < numMilliSecondsPerHour * numHours;
    }

    private boolean isAdAvailable() {
        return (appOpenAd != null) && wasLoadTimeLessThanNHoursAgo(4);
    }

    // ================================
    // Show Ad (wrapper)
    // ================================
    public void showAdIfAvailable(Activity activity) {
        if (!isEnableShowAds) {
            Log.e(TAG, "return not show ads: ");
            isEnableShowAds = true;
            return;
        }
        Log.e(TAG, "showAdIfAvailable: ");
        showAdIfAvailable(activity, () -> {
            // empty
        });
    }

    // ================================
    // Show Ad - main logic
    // ================================
    public void showAdIfAvailable(Activity activity, OnShowAdCompleteListener listener) {

        if (activity == null || activity.isFinishing()) return;

        if (isShowingAd || isAdFullShowing) {
            Log.d(TAG, "The app open ad is already showing.");
            return;
        }

        if (!isAdAvailable() || PurchaseUtils.isNoAds(activity) || !FirebaseQuery.getEnableAds() || !FirebaseQuery.getEnableOpenResume()) {
            Log.d(TAG, "The app open ad is not ready yet.");
            listener.onShowAdComplete();
            loadAd(activity);
            return;
        }
        Log.d(TAG, "Will show ad.");

        appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {

            @Override
            public void onAdDismissedFullScreenContent() {
                Log.d(TAG, "onAdDismissedFullScreenContent.");
                shouldReloadAd = true;
                appOpenAd = null;
                isShowingAd = false;

                DialogUtils.dismissDialogResume();
                listener.onShowAdComplete();
                loadAd(activity);
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                Log.d(TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());
                appOpenAd = null;
                isShowingAd = false;

                listener.onShowAdComplete();
                loadAd(activity);
            }

            @Override
            public void onAdShowedFullScreenContent() {
                Log.d(TAG, "onAdShowedFullScreenContent.");
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

        // Kotlin coroutine → Java Handler
        new Handler(Looper.getMainLooper()).post(() -> {
            DialogUtils.showDialogResume(activity);
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                isShowingAd = true;
                appOpenAd.show(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 500);
    }

}
