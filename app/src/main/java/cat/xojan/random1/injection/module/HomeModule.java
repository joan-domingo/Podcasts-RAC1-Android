package cat.xojan.random1.injection.module;

import android.app.Activity;

import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.presenter.HomePresenter;
import cat.xojan.random1.viewmodel.PodcastsViewModel;
import cat.xojan.random1.viewmodel.ProgramsViewModel;
import cat.xojan.random1.viewmodel.SectionsViewModel;
import dagger.Module;
import dagger.Provides;

@Module
public class HomeModule {

    private final Activity mActivity;

    public HomeModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    HomePresenter provideHomePresenter(ProgramDataInteractor programDataInteractor) {
        return new HomePresenter(mActivity, programDataInteractor);
    }

    @Provides
    ProgramsViewModel provideProgramsViewModel(ProgramDataInteractor programDataInteractor) {
        return new ProgramsViewModel(programDataInteractor);
    }

    @Provides
    PodcastsViewModel providePOdcastsViewModel(ProgramDataInteractor programDataInteractor) {
        return new PodcastsViewModel(programDataInteractor);
    }

    @Provides
    SectionsViewModel provideSectionsViewModel(ProgramDataInteractor programDataInteractor) {
        return new SectionsViewModel(programDataInteractor);
    }
}
