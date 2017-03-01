package cat.xojan.random1;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.VisibleForTesting;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;

import cat.xojan.random1.commons.Log;
import cat.xojan.random1.injection.component.AppComponent;
import cat.xojan.random1.injection.component.DaggerAppComponent;
import cat.xojan.random1.injection.module.AppModule;
import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {

    private static final String TAG = Application.class.getSimpleName();
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
        initInjector();
        initLeakDetection();

        removePreviousDownloadedPodcasts();
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

    private void removePreviousDownloadedPodcasts() {
        boolean arePodcastsRemoved = getSharedPreferences("preferences", Context.MODE_PRIVATE)
                .getBoolean("old_downloaded_podcasts", false);
        if (!arePodcastsRemoved) {
            getSharedPreferences("preferences", Context.MODE_PRIVATE).edit()
                    .putBoolean("old_downloaded_podcasts", true).apply();
            Log.d(TAG, "removing old podcasts");

            removeDownloaded();
            removeDownloading();
        }
    }

    private void removeDownloaded() {
        File iternalFileDirectory = getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        for (File podcastFile : iternalFileDirectory.listFiles()) {
            if (podcastFile.delete()) {
                Log.d(TAG, "delete downloaded podcast");
            }
        }
    }

    private void removeDownloading() {
        File iternalFileDirectory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        for (File podcastFile : iternalFileDirectory.listFiles()) {
            if (podcastFile.delete()) {
                Log.d(TAG, "delete downloading podcast");
            }
        }
    }

    @VisibleForTesting
    public void setTestComponent(AppComponent testingComponent) {
        mAppComponent = testingComponent;
    }
}
