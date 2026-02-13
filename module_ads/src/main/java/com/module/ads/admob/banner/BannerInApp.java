package com.module.ads.admob.banner;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.module.ads.remote.FirebaseQuery;
import com.module.ads.utils.AdUtils;
import com.module.ads.utils.FBTracking;
import com.module.ads.utils.PurchaseUtils;


public class BannerInApp {

    private static BannerInApp bannerInApp;

    public static BannerInApp getInstance() {
        if (bannerInApp == null) {
            bannerInApp = new BannerInApp();
        }
        return bannerInApp;
    }

    private boolean isLoadingAd = false;

    public void loadAndShow(final Activity activity, final LinearLayout lnBanner, String idBanner) {
        if (isLoadingAd) return;
        if (!PurchaseUtils.isNoAds(activity) && FirebaseQuery.getEnableAds() && FirebaseQuery.getEnableBanner()) {
            isLoadingAd = true;
            AdView mAdView = new AdView(activity);
            mAdView.setAdSize(AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, BannerUtils.getAdWidth(activity)));

            AdUtils.onPaidEvent(activity, mAdView);
            mAdView.setAdUnitId(idBanner);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    isLoadingAd = false;
                    if (lnBanner != null) {
                        lnBanner.setVisibility(ViewGroup.VISIBLE);
                        lnBanner.removeAllViews();
                        lnBanner.addView(mAdView);
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    isLoadingAd = false;
                    lnBanner.setVisibility(View.GONE);
                }

                @Override
                public void onAdImpression() {
                    FBTracking.funcTrackingIAA(activity, FBTracking.EVENT_AD_IMPRESSION);
                }
            });

            mAdView.loadAd(BannerUtils.getAdRequest(FirebaseQuery.getEnableBannerCollapse()));
        } else {
            isLoadingAd = false;
            lnBanner.setVisibility(View.GONE);
        }
    }
}
