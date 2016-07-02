package cat.xojan.random1.injection.module;

import cat.xojan.random1.data.NetworkPodcastRepository;
import cat.xojan.random1.data.StaticProgramRepository;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.presenter.LatestPodcastPresenter;
import cat.xojan.random1.ui.presenter.ProgramsPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    @Provides
    LatestPodcastPresenter provideLatestPodcastPresenter(PodcastDataInteractor podcastDataInteractor) {
        return new LatestPodcastPresenter(podcastDataInteractor);
    }

    @Provides
    PodcastDataInteractor providePodcastDataInteractor() {
        return new PodcastDataInteractor(new NetworkPodcastRepository());
    }

    @Provides
    ProgramsPresenter provideProgramsPresenter(ProgramDataInteractor programDataInteractor) {
        return new ProgramsPresenter(programDataInteractor);
    }

    @Provides
    ProgramDataInteractor provideProgramDataInteractor() {
        return new ProgramDataInteractor(new StaticProgramRepository());
    }
}
