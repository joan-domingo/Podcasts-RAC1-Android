package cat.xojan.random1.injection.module;

import android.app.DownloadManager;
import android.content.Context;

import javax.inject.Singleton;

import cat.xojan.random1.Application;
import cat.xojan.random1.data.RAC1PodcastRepository;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import dagger.Module;
import dagger.Provides;

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

    @Provides
    @Singleton
    DownloadManager provideDownloadManager() {
        return (DownloadManager) mApplication.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Provides
    @Singleton
    PodcastDataInteractor providePodcastDataInteractor() {
        return new PodcastDataInteractor(new RAC1PodcastRepository(), mApplication);
    }
}
