package cat.xojan.random1;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.squareup.leakcanary.LeakCanary;

import cat.xojan.random1.injection.component.AppComponent;
import cat.xojan.random1.injection.component.DaggerAppComponent;
import cat.xojan.random1.injection.module.AppModule;
import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    protected AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
        initInjector();
        initLeakDetection();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private void initInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    private void initLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    private void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Answers(), new Crashlytics());
        }
    }
}
