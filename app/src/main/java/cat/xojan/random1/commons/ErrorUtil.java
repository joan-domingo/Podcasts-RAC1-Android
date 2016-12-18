package cat.xojan.random1.commons;

import com.crashlytics.android.Crashlytics;

import cat.xojan.random1.Log;

public class ErrorUtil {

    public static void logException(Throwable e) {
        try {
            Crashlytics.logException(e);
        } catch (IllegalStateException exception) {
            e.printStackTrace();
        }
    }

    public static void logException(String message) {
        try {
            Crashlytics.log(message);
        } catch (IllegalStateException e) {
            Log.w("DownloadManager", message);
        }
    }
}
