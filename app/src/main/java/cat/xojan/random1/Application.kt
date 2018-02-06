package cat.xojan.random1

import android.support.multidex.MultiDexApplication
import cat.xojan.random1.injection.component.AppComponent
import cat.xojan.random1.injection.component.DaggerAppComponent
import cat.xojan.random1.injection.module.AppModule
import com.squareup.leakcanary.LeakCanary
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins



class Application : MultiDexApplication() {

     lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        initInjector()
        initLeakDetection()
        setErrorHandler()
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

    private fun setErrorHandler() {
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                e.printStackTrace()
            }
            if (e is InterruptedException) {
                e.printStackTrace()
            }
        }
    }
}