package cat.xojan.random1;

/** Android Log wrapper class. */
public class Log {

    private static final boolean IS_DEBUG = BuildConfig.DEBUG;
    private static final String APP_TAG = "PodcastsRAC1";

    /** Send a DEBUG log message. */
    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.d(APP_TAG + "-" + tag, msg);
        }
    }

    /** Send a VERBOSE log message. */
    public static void v(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.v(APP_TAG + "-" + tag, msg);
        }
    }

    /** Send an INFO log message. */
    public static void i(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.i(APP_TAG + "-" + tag, msg);
        }

    }

    /** Send a WARN log message. */
    public static void w(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.w(APP_TAG + "-" + tag, msg);
        }
    }

    /** Send an ERROR log message. */
    public static void e(String tag, String msg) {
        if (IS_DEBUG) {
            android.util.Log.e(APP_TAG + "-" + tag, msg);
        }
    }
}
