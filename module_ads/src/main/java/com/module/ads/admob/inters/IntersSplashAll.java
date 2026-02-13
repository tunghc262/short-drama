package com.module.ads.admob.inters;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.module.ads.admob.banner.BannerSplash;
import com.module.ads.callback.CallbackAd;
import com.module.ads.remote.FirebaseQuery;
import com.module.ads.utils.AdUtils;
import com.module.ads.utils.DialogUtils;
import com.module.ads.utils.FBTracking;
import com.module.ads.utils.PurchaseUtils;


public class IntersSplashAll {

    private static IntersSplashAll intersSplashAll;
    private boolean isTimeOut = false;
    public static InterstitialAd interstitialAdHigh;
    public static InterstitialAd interstitialAdHigh2;
    public static InterstitialAd interstitialAdHigh3;
    public static InterstitialAd interstitialAdNormal;
    private boolean isLoadingHigh = false;
    private boolean isLoadingHigh2 = false;
    private boolean isLoadingHigh3 = false;
    private boolean isLoadingNormal = false;
    public boolean isShowing = false;

    public static IntersSplashAll getInstance() {
        if (intersSplashAll == null) {
            intersSplashAll = new IntersSplashAll();
        }
        return intersSplashAll;
    }


    public void loadAnsShow(final Activity activity, CallbackAd mCallBack) {
        isTimeOut = false;
        if (FirebaseQuery.getEnableIntersSplashHigh()) {
            loadAdsHigh(activity, mCallBack);
        } else if (FirebaseQuery.getEnableIntersSplash()) {
            loadAdsNormal(activity, mCallBack);
        } else {
            mCallBack.onNextAction();
        }
        timeout(activity, mCallBack);
    }

    private void loadAdsHigh(Activity activity, CallbackAd mCallBack) {
        if (interstitialAdHigh != null || isLoadingHigh) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters()) {
            if (FirebaseQuery.getEnableIntersSplashHigh()) {
                Log.e("TamBT", "loadAdsHigh: inter splash high");
                isLoadingHigh = true;
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, FirebaseQuery.getIdIntersSplashHigh(), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                Log.e("TamBT", "onAdLoaded: inter splash high");
                                isLoadingHigh = false;
                                interstitialAdHigh = interstitialAd;
                                isTimeOut = true;
                                AdUtils.onPaidEvent(activity, interstitialAd);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        Log.e("TamBT", "onAdFailedToShowFullScreenContent: inter splash high");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        isShowing = false;
                                        destroyAd();
                                        DialogUtils.dismissDialogLoading();
                                        if (mCallBack != null) {
                                            mCallBack.onNextAction();
                                        }
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        isShowing = true;
                                        FBTracking.funcTracking(activity, "inter_splash_view", null);
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showDialogLoading(activity);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (activity != null && !activity.isFinishing()) {
                                                    if (!BannerSplash.isLoaded) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log.e("TamBT", "show inters splash: high delay");
                                                                interstitialAd.show(activity);
                                                            }
                                                        }, 3000L);
                                                    } else {
                                                        Log.e("TamBT", " show inters splash: high not delay");
                                                        interstitialAd.show(activity);
                                                    }
                                                }
                                            }
                                        }, 500L);
                                    }
                                }, 3000L);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.e("TamBT", "onAdFailedToLoad: inter splash high " + loadAdError.getMessage());
                                isLoadingHigh = false;
                                interstitialAdHigh = null;
                                int numberOf2Floor = (int) FirebaseQuery.getNumberOf2Floor();
                                if (numberOf2Floor == 3 || numberOf2Floor == 4) {
                                    loadAdsHigh2(activity, mCallBack);
                                } else {
                                    if (FirebaseQuery.getEnableIntersSplash()) {
                                        loadAdsNormal(activity, mCallBack);
                                    } else {
                                        if (mCallBack != null) {
                                            mCallBack.onNextAction();
                                        }
                                    }
                                }
                            }
                        });
            } else {
                isLoadingHigh = false;
                if (FirebaseQuery.getEnableIntersSplash()) {
                    loadAdsNormal(activity, mCallBack);
                }
            }
        } else {
            if (mCallBack != null) {
                mCallBack.onNextAction();
            }
        }
    }

    private void loadAdsHigh2(Activity activity, CallbackAd mCallBack) {
        if (interstitialAdHigh2 != null || isLoadingHigh2) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters()) {
            if (FirebaseQuery.getEnableIntersSplashHigh()) {
                Log.e("TamBT", "loadAdsHigh: inter splash 2 high");
                isLoadingHigh2 = true;
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, FirebaseQuery.getIdIntersSplash2High(), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                Log.e("TamBT", "onAdLoaded: inter splash 2 high");
                                isLoadingHigh2 = false;
                                interstitialAdHigh2 = interstitialAd;
                                isTimeOut = true;
                                AdUtils.onPaidEvent(activity, interstitialAd);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        Log.e("TamBT", "onAdFailedToShowFullScreenContent: inter splash 2 high");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        isShowing = false;
                                        destroyAd();
                                        DialogUtils.dismissDialogLoading();
                                        if (mCallBack != null) {
                                            mCallBack.onNextAction();
                                        }
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        isShowing = true;
                                        FBTracking.funcTracking(activity, "inter_splash_view", null);
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showDialogLoading(activity);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (activity != null && !activity.isFinishing()) {
                                                    if (!BannerSplash.isLoaded) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log.e("TamBT", "show inters splash 2: high delay");
                                                                interstitialAd.show(activity);
                                                            }
                                                        }, 3000L);
                                                    } else {
                                                        Log.e("TamBT", " show inters splash 2 : high not delay");
                                                        interstitialAd.show(activity);
                                                    }
                                                }
                                            }
                                        }, 500L);
                                    }
                                }, 3000L);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.e("TamBT", "onAdFailedToLoad: inter splash 2 high"  + loadAdError.getMessage());
                                isLoadingHigh2 = false;
                                interstitialAdHigh2 = null;
                                int numberOf2Floor = (int) FirebaseQuery.getNumberOf2Floor();
                                if (numberOf2Floor == 4) {
                                    loadAdsHigh3(activity, mCallBack);
                                } else {
                                    if (FirebaseQuery.getEnableIntersSplash()) {
                                        loadAdsNormal(activity, mCallBack);
                                    } else {
                                        if (mCallBack != null) {
                                            mCallBack.onNextAction();
                                        }
                                    }
                                }
                            }
                        });
            } else {
                isLoadingHigh2 = false;
                if (FirebaseQuery.getEnableIntersSplash()) {
                    loadAdsNormal(activity, mCallBack);
                }
            }
        } else {
            if (mCallBack != null) {
                mCallBack.onNextAction();
            }
        }
    }

    private void loadAdsHigh3(Activity activity, CallbackAd mCallBack) {
        if (interstitialAdHigh3 != null || isLoadingHigh3) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters()) {
            if (FirebaseQuery.getEnableIntersSplashHigh()) {
                Log.e("TamBT", "loadAdsHigh: inter splash 3 high");
                isLoadingHigh3 = true;
                AdRequest adRequest = new AdRequest.Builder().build();
                InterstitialAd.load(activity, FirebaseQuery.getIdIntersSplash3High(), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                Log.e("TamBT", "onAdLoaded: inter splash 3 high");
                                isLoadingHigh3 = false;
                                interstitialAdHigh3 = interstitialAd;
                                isTimeOut = true;
                                AdUtils.onPaidEvent(activity, interstitialAd);
                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                        Log.e("TamBT", "onAdFailedToShowFullScreenContent: inter splash 3 high");
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        isShowing = false;
                                        destroyAd();
                                        DialogUtils.dismissDialogLoading();
                                        if (mCallBack != null) {
                                            mCallBack.onNextAction();
                                        }
                                    }

                                    @Override
                                    public void onAdImpression() {
                                        FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        isShowing = true;
                                        FBTracking.funcTracking(activity, "inter_splash_view", null);
                                    }
                                });

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showDialogLoading(activity);
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (activity != null && !activity.isFinishing()) {
                                                    if (activity != null && !activity.isFinishing()) {
                                                        if (!BannerSplash.isLoaded) {
                                                            new Handler().postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Log.e("TamBT", "show inters splash 3: high delay");
                                                                    interstitialAd.show(activity);
                                                                }
                                                            }, 3000L);
                                                        } else {
                                                            Log.e("TamBT", " show inters splash 3 : high not delay");
                                                            interstitialAd.show(activity);
                                                        }
                                                    }
                                                }
                                            }
                                        }, 500L);
                                    }
                                }, 3000L);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                Log.e("TamBT", "onAdFailedToLoad: inter splash 3 high" + loadAdError.getMessage());
                                isLoadingHigh3 = false;
                                interstitialAdHigh3 = null;
                                if (FirebaseQuery.getEnableIntersSplash()) {
                                    loadAdsNormal(activity, mCallBack);
                                } else {
                                    if (mCallBack != null) {
                                        mCallBack.onNextAction();
                                    }
                                }
                            }
                        });
            } else {
                isLoadingHigh3 = false;
                if (FirebaseQuery.getEnableIntersSplash()) {
                    loadAdsNormal(activity, mCallBack);
                }
            }
        } else {
            if (mCallBack != null) {
                mCallBack.onNextAction();
            }
        }
    }

    private void loadAdsNormal(Activity activity, CallbackAd mCallBack) {
        if (interstitialAdNormal != null || isLoadingNormal) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableInters() && FirebaseQuery.getEnableIntersSplash()) {
            Log.e("TamBT", "loadAdsNormal: inter splash normal");
            isLoadingNormal = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(activity, FirebaseQuery.getIdIntersSplash(), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            Log.e("TamBT", "onAdLoaded: inter splash normal");
                            isTimeOut = true;
                            isLoadingNormal = false;
                            interstitialAdNormal = interstitialAd;
                            AdUtils.onPaidEvent(activity, interstitialAd);
                            interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    Log.e("TamBT", "onAdFailedToShowFullScreenContent: inter splash normal");
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    isShowing = false;
                                    destroyAd();
                                    DialogUtils.dismissDialogLoading();
                                    if (mCallBack != null) {
                                        mCallBack.onNextAction();
                                    }
                                }

                                @Override
                                public void onAdImpression() {
                                    FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    isShowing = true;
                                    FBTracking.funcTracking(activity, "inter_splash_view", null);
                                }
                            });
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    DialogUtils.showDialogLoading(activity);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (activity != null && !activity.isFinishing()) {
                                                if (activity != null && !activity.isFinishing()) {
                                                    if (!BannerSplash.isLoaded) {
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Log.e("TamBT", "show inters splash normal delay");
                                                                interstitialAd.show(activity);
                                                            }
                                                        }, 3000L);
                                                    } else {
                                                        Log.e("TamBT", " show inters splash normal not delay");
                                                        interstitialAd.show(activity);
                                                    }
                                                }
                                            }
                                        }
                                    }, 500L);
                                }
                            }, 3000L);

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.e("TamBT", "onAdFailedToLoad: inter splash normal" + loadAdError.getMessage());
                            isTimeOut = true;
                            destroyAd();
                            if (mCallBack != null) {
                                mCallBack.onNextAction();
                            }
                        }
                    });
        } else {
            if (mCallBack != null) {
                mCallBack.onNextAction();
            }
        }
    }

    private void timeout(Activity activity, CallbackAd callBack) {
        CountDownTimer countDownTimer = new CountDownTimer(30000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (!isTimeOut) {
                    if (activity != null && !activity.isFinishing()) {
                        if (interstitialAdHigh != null) {
                            interstitialAdHigh.show(activity);
                            cancel();
                            return;
                        }
                        if (interstitialAdHigh2 != null) {
                            interstitialAdHigh2.show(activity);
                            cancel();
                            return;
                        }
                        if (interstitialAdHigh3 != null) {
                            interstitialAdHigh3.show(activity);
                            cancel();
                            return;
                        }
                        if (interstitialAdNormal != null) {
                            interstitialAdNormal.show(activity);
                            cancel();
                            return;
                        }
                        if (callBack != null) {
                            callBack.onNextAction();
                            cancel();
                        }
                    }
                }
            }
        };
        countDownTimer.start();
    }

    public void showAdsAll(Activity activity) {
        try {
            if (activity != null && !activity.isFinishing()) {
                DialogUtils.dismissDialogLoading();
                InterstitialAd interstitialAd = null;
                if (interstitialAdHigh != null) {
                    interstitialAd = interstitialAdHigh;
                } else if (interstitialAdHigh2 != null) {
                    interstitialAd = interstitialAdHigh2;
                } else if (interstitialAdHigh3 != null) {
                    interstitialAd = interstitialAdHigh3;
                } else if (interstitialAdNormal != null) {
                    interstitialAd = interstitialAdNormal;
                }
                if (interstitialAd != null) {
                    interstitialAd.show(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void destroyAd() {
        isLoadingHigh = false;
        isLoadingHigh2 = false;
        isLoadingHigh3 = false;
        isLoadingNormal = false;
        interstitialAdHigh = null;
        interstitialAdHigh2 = null;
        interstitialAdHigh3 = null;
        interstitialAdNormal = null;
    }
}
