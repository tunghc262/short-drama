package com.module.ads.utils;

import android.app.Activity;

import com.module.ads.views.LoadingAdDialog;
import com.module.ads.views.ResumeAdDialog;


public class DialogUtils {
    private static LoadingAdDialog loadingAdDialog;
    private static ResumeAdDialog resumeAdDialog;

    public static void showDialogLoading(Activity activity) {
        try {
            if (loadingAdDialog == null) {
                loadingAdDialog = new LoadingAdDialog(activity);
            }
            if (activity != null) {
                loadingAdDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialogLoading() {
        try {
            if (loadingAdDialog != null && loadingAdDialog.isShowing()) {
                loadingAdDialog.dismiss();
                loadingAdDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDialogResume(Activity activity) {
        try {
            if (resumeAdDialog == null) {
                resumeAdDialog = new ResumeAdDialog(activity);
            }
            if (activity != null) {
                resumeAdDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dismissDialogResume() {
        try {
            if (resumeAdDialog != null && resumeAdDialog.isShowing()) {
                resumeAdDialog.dismiss();
                resumeAdDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
