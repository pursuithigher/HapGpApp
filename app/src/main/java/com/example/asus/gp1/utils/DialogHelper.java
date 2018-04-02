package com.example.asus.gp1.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

/**
 * Created by Qzhu on 2018/3/29.
 */

public class DialogHelper {
    private AlertDialog dialog;

    public DialogHelper createProcessDialog(Activity contextThemeWrapper, String message) {
        dismiss();
        dialog = new ProgressDialog(contextThemeWrapper);
        dialog.setMessage(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return this;
    }

    public void show(boolean cancelable, boolean indeterminate) {
        if (null == dialog) {
            return;
        }
        dialog.setCancelable(cancelable);
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
}
