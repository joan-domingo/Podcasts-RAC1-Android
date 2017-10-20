package cat.xojan.random1

import cat.xojan.random1.injection.component.AppComponent
import cat.xojan.random1.injection.component.DaggerAppComponent
import cat.xojan.random1.injection.module.AppModule
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.squareup.leakcanary.LeakCanary
import io.fabric.sdk.android.Fabric

class Application : android.app.Application() {

     lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initCrashlytics()
        initInjector()
        initLeakDetection()
    }

    private fun initInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
        }
    }

    private fun initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Answers(), Crashlytics())
        }
    }
}