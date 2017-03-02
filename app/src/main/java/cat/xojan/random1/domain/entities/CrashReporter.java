package cat.xojan.random1.domain.entities;

import com.crashlytics.android.Crashlytics;

public class CrashReporter {

    private Crashlytics mCrashlytics;

    public CrashReporter() {
    }

    public CrashReporter(Crashlytics crashlytics) {
        mCrashlytics = crashlytics;
    }

    public void logException(Throwable e) {
        if (mCrashlytics != null) mCrashlytics.logException(e);
    }

    public void logException(String message) {
        if (mCrashlytics != null) mCrashlytics.log(message);
    }
}
