package cat.xojan.random1.injection;

import android.app.DownloadManager;

import com.crashlytics.android.answers.Answers;

import javax.inject.Singleton;

import cat.xojan.random1.TestApplication;
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

import static org.mockito.Mockito.mock;

@Module
public class AppTestModule {

    private static final String RAC1_URL = "http://www.rac1.cat/audioteca/api/app/";

    private final TestApplication mTestApplication;

    public AppTestModule(TestApplication application) {
        mTestApplication = application;
    }

    @Provides
    DownloadManager downloadManager() {
        return mock(DownloadManager.class);
    }

    @Provides
    ProgramDataInteractor programDataInteractor(Rac1RetrofitService service) {
        return new ProgramDataInteractor(new RemoteProgramRepository(service),
                new PreferencesDownloadPodcastRepository(mTestApplication),
                mTestApplication, mock(DownloadManager.class));
    }

    @Provides
    Answers answers() {
        return mock(Answers.class);
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
}
