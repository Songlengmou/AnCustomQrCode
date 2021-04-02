package com.anningtex.ancustomqrcode.decoding;

import android.app.Activity;
import android.content.DialogInterface;

/**
 * @author Song
 * desc:Simple listener used to exit the app in a few cases.
 */
public final class FinishListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener, Runnable {
    private final Activity activityToFinish;

    public FinishListener(Activity activityToFinish) {
        this.activityToFinish = activityToFinish;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        run();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        run();
    }

    @Override
    public void run() {
        activityToFinish.finish();
    }
}
