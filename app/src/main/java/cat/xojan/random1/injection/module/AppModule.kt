package cat.xojan.random1.injection.module

import android.app.DownloadManager
import android.content.Context
import cat.xojan.random1.Application
import cat.xojan.random1.BuildConfig
import cat.xojan.random1.data.*
import cat.xojan.random1.domain.interactor.PodcastDataInteractor
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.domain.model.CrashReporter
import cat.xojan.random1.domain.model.EventLogger
import cat.xojan.random1.domain.repository.PodcastPreferencesRepository
import cat.xojan.random1.domain.repository.PodcastRepository
import cat.xojan.random1.domain.repository.ProgramRepository
import cat.xojan.random1.feature.mediaplayback.MediaProvider
import cat.xojan.random1.feature.mediaplayback.QueueManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    companion object {
        private const val BASE_URL = BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun provideDownloadManager(): DownloadManager {
        return application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    @Provides
    @Singleton
    fun provideRetrofitRac1Service(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClientBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) httpClientBuilder.addInterceptor(loggingInterceptor)

        val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build()

        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideProgramDataInteractor(programRepository: ProgramRepository) : ProgramDataInteractor {
        return ProgramDataInteractor(programRepository)
    }

    @Provides
    @Singleton
    fun providePodcastDataInteractor(podcastRepository: PodcastRepository,
                                     programRepository: ProgramRepository,
                                     podcastPrefRepository: PodcastPreferencesRepository,
                                     downloadManager: DownloadManager,
                                     eventLogger: EventLogger)
            : PodcastDataInteractor {
        return PodcastDataInteractor(
                programRepository,
                podcastRepository,
                podcastPrefRepository,
                downloadManager,
                application,
                SharedPrefDownloadPodcastRepository(application),
                eventLogger)
    }

    @Provides
    @Singleton
    fun provideEventLogger(): EventLogger {
        return EventLogger(FirebaseAnalytics.getInstance(application))
    }

    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter {
        return CrashReporter()
    }

    @Provides
    @Singleton
    fun provideMediaProvider(programDataInteractor: ProgramDataInteractor,
                             podcastDataInteractor: PodcastDataInteractor,
                             queueManager: QueueManager
    ): MediaProvider {
        return MediaProvider(programDataInteractor, podcastDataInteractor, queueManager)
    }

    @Provides
    @Singleton
    fun provideRemoteProgramRepository(service: ApiService): ProgramRepository {
        return RemoteProgramRepository(service)
    }

    @Provides
    @Singleton
    fun provideRemotePodcastRepository(service: ApiService): PodcastRepository {
        return RemotePodcastRepository(service)
    }

    @Provides
    fun providesPodcastsPreferencesRepository(): PodcastPreferencesRepository {
        return SharedPrefPodcastPreferencesRepository(application)
    }

    @Provides
    @Singleton
    fun provideQueueManager(eventLogger: EventLogger): QueueManager {
        return QueueManager(eventLogger)
    }
}