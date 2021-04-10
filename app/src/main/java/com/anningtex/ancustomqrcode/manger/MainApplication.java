package com.anningtex.ancustomqrcode.manger;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * @Author Song
 */
public class MainApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static String SAVE_SUCCESS_PATH;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=fa71eda3");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        new Handler(getMainLooper()).post(() -> {
//            while (true) {
//                try {
//                    Looper.loop();//try-catch主线程的所有异常；Looper.loop()内部是一个死循环，出现异常时才会退出，所以这里使用while(true)。
//                } catch (Throwable e) {
//                    Log.d(TAG, "Looper.loop(): " + e.getMessage());
//                }
//            }
//        });
//        //try-catch子线程的所有异常。
//        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            Log.d(TAG, "UncaughtExceptionHandler: " + e.getMessage());
//        });
    }
}
