package com.module.ads.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.FirebaseApp;
import com.module.ads.admob.aoa.ResumeAdsManager;
import com.module.ads.admob.inters.IntersInApp;
import com.module.ads.admob.inters.IntersSplashAll;
import com.module.ads.admob.natives.NativeOnboardFullscreen1;
import com.module.ads.admob.natives.NativeOnboardFullscreen2;
import com.module.ads.admob.reward.RewardInApp;
import com.module.ads.mmp.AdjustTracking;
import com.module.ads.utils.DialogUtils;
import com.module.ads.utils.SharePreferUtils;

import java.util.ArrayList;
import java.util.List;

public class ModuleApplication extends Application implements Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private Activity currentActivity;

    private final List<Class<?>> excludedActivities = new ArrayList<>();

    public void addExcludedActivity(Class<?> activityClass) {
        excludedActivities.add(activityClass);
    }

    public boolean isActivityExcluded(Activity activity) {
        return excludedActivities.contains(activity.getClass());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        AdjustTracking.initAdjust(this);
        SharePreferUtils.init(this);

        registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    private boolean isAdsActivity(Activity activity) {
        if (activity == null) return true;
        String name = activity.getClass().getName();
        return name.startsWith("com.google.android.gms.ads") ||
                name.startsWith("com.bytedance.sdk.openadsdk") ||
                name.startsWith("com.applovin") ||
                name.startsWith("com.ironsource") ||
                name.startsWith("com.unity3d") ||
                name.startsWith("com.facebook.ads") ||
                name.startsWith("com.mbridge.msdk") ||
                name.startsWith("com.vungle");
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        if (currentActivity == null) return;
        if (isActivityExcluded(currentActivity)) {
            return;
        }
        if (IntersSplashAll.getInstance().isShowing ||
                IntersInApp.getInstance().isShowing ||
                RewardInApp.getInstance().isShowing ||
                NativeOnboardFullscreen1.getInstance().isShowing ||
                NativeOnboardFullscreen2.getInstance().isShowing
        ) {
            return;
        }
        Log.e("ResumeAdsManager", "onStart: showAdIfAvailable");
        ResumeAdsManager.getOpenAds().showAdIfAvailable(currentActivity);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        Log.e("ResumeAdsManager", "onActivityStarted: 1");
        if (!ResumeAdsManager.getOpenAds().isShowingAd) {
            currentActivity = activity;
        }
        if (isAdsActivity(currentActivity)) return;
        if (!isActivityExcluded(currentActivity)) {
            Log.e("ResumeAdsManager", "onActivityStarted: 2");
            ResumeAdsManager.getOpenAds().loadAd(currentActivity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        Log.e("ResumeAdsManager", "onActivityResumed: 1");
        if (currentActivity != null) {
            if (isActivityExcluded(currentActivity)) {
                Log.e("ResumeAdsManager", "onActivityResumed: 2");
                return;
            }
            if (ResumeAdsManager.getOpenAds().appOpenAd == null &&
                    !ResumeAdsManager.getOpenAds().isShowingAd &&
                    !ResumeAdsManager.getOpenAds().isLoadingAd
            ) {
                Log.e("ResumeAdsManager", "onActivityResumed: 3");
                ResumeAdsManager.getOpenAds().loadAd(activity);
            }
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        DialogUtils.dismissDialogLoading();
        DialogUtils.dismissDialogResume();
    }
}
