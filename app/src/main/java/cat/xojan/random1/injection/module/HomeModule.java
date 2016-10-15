package cat.xojan.random1.injection.module;

import android.app.Activity;
import android.app.DownloadManager;

import cat.xojan.random1.data.RAC1ProgramRepository;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.PerActivity;
import cat.xojan.random1.presenter.DownloadsPresenter;
import cat.xojan.random1.presenter.PodcastListPresenter;
import cat.xojan.random1.presenter.ProgramsPresenter;
import cat.xojan.random1.presenter.SectionPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private final Activity mActivity;

    public HomeModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    PodcastListPresenter provideLatestPodcastPresenter(PodcastDataInteractor podcastDataInteractor,
                                                       DownloadManager downloadManager) {
        return new PodcastListPresenter(podcastDataInteractor, mActivity, downloadManager);
    }

    @Provides
    ProgramsPresenter provideProgramsPresenter(ProgramDataInteractor programDataInteractor) {
        return new ProgramsPresenter(programDataInteractor);
    }

    @Provides
    @PerActivity
    ProgramDataInteractor provideProgramDataInteractor() {
        return new ProgramDataInteractor(new RAC1ProgramRepository(), mActivity);
    }

    @Provides
    @PerActivity
    DownloadsPresenter provideHomePresenter(PodcastDataInteractor podcastDataInteractor) {
        return new DownloadsPresenter(podcastDataInteractor);
    }

    @Provides
    SectionPresenter provideSectionPresenter(ProgramDataInteractor programDataInteractor) {
        return new SectionPresenter(programDataInteractor);
    }
}
