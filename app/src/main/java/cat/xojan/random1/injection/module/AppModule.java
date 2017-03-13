package cat.xojan.random1.injection.module;

import android.app.DownloadManager;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import cat.xojan.random1.Application;
import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.data.Rac1RetrofitService;
import cat.xojan.random1.data.RemoteProgramRepository;
import cat.xojan.random1.domain.entities.CrashReporter;
import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RAC1_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(Rac1RetrofitService.class);
    }

    @Provides @Singleton
    ProgramDataInteractor provideProgramDataInteractor(Rac1RetrofitService service,
                                                       DownloadManager downloadManager) {
        return new ProgramDataInteractor(new RemoteProgramRepository(service),
                new PreferencesDownloadPodcastRepository(mApplication),
                mApplication, downloadManager);
    }

    @Provides @Singleton
    EventLogger provideEventLogger() {
        if (BuildConfig.DEBUG) return new EventLogger();
        return new EventLogger(Answers.getInstance());
    }

    @Provides @Singleton
    CrashReporter provideCrashReporter() {
        if (BuildConfig.DEBUG) return new CrashReporter();
        return new CrashReporter(Crashlytics.getInstance());
    }
}
