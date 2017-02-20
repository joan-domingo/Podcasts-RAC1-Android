package cat.xojan.random1.injection.module;

import android.app.DownloadManager;
import android.content.Context;

import javax.inject.Singleton;

import cat.xojan.random1.Application;
import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.data.Rac1RetrofitService;
import cat.xojan.random1.data.RemoteProgramRepository;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A module for Android-specific dependencies which require a {@link Context} or
 * {@link android.app.Application} to create.
 */
@Module
public class AppModule {

    private static final String RAC1_URL = "http://www.rac1.cat/audioteca/api/app/";

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides @Singleton
    DownloadManager provideDownloadManager() {
        return (DownloadManager) mApplication.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Provides @Singleton
    Rac1RetrofitService provideRetrofitRac1Service() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RAC1_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(Rac1RetrofitService.class);
    }

    @Provides @Singleton
    ProgramDataInteractor provideProgramDataInteractor(Rac1RetrofitService service) {
        return new ProgramDataInteractor(new RemoteProgramRepository(service),
                new PreferencesDownloadPodcastRepository(mApplication), mApplication);
    }
}
