package cat.xojan.random1.commons;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import cat.xojan.random1.BuildConfig;

public class ErrorUtil {

    public static void logException(Throwable e) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace();
        } else {
            Crashlytics.logException(e);
        }
    }

    public static void logException(String message) {
        if (BuildConfig.DEBUG) {
            Log.w("DownloadManager", message);
        } else {
            Crashlytics.log(message);
        }
    }
}
