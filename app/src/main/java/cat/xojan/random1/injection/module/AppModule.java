package cat.xojan.random1.injection.module;

import android.content.Context;

import cat.xojan.random1.Application;
import dagger.Module;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class AppModule {
    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }
}
