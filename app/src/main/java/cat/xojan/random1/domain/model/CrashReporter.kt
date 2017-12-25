package cat.xojan.random1.domain.model

import com.crashlytics.android.Crashlytics


class CrashReporter {

    fun logException(e: Throwable) {
        Crashlytics.logException(e)
    }

    fun logException(message: String) {
        Crashlytics.log(message)
    }
}