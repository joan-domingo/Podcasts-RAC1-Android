package cat.xojan.random1.domain.model

import com.google.firebase.crashlytics.FirebaseCrashlytics


class CrashReporter {

    val crashlytics = FirebaseCrashlytics.getInstance()


    fun logException(e: Throwable) {
        crashlytics.recordException(e)
    }

    fun logException(message: String) {
        crashlytics.log(message)
    }
}