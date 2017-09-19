package cat.xojan.random1.injection.module

import android.app.Activity
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.viewmodel.PodcastsViewModel
import cat.xojan.random1.viewmodel.ProgramsViewModel
import cat.xojan.random1.viewmodel.SectionsViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule(private val mActivity: Activity) {

    @Provides
    internal fun provideProgramsViewModel(programDataInteractor: ProgramDataInteractor): ProgramsViewModel {
        return ProgramsViewModel(programDataInteractor)
    }

    @Provides
    internal fun providePOdcastsViewModel(programDataInteractor: ProgramDataInteractor): PodcastsViewModel {
        return PodcastsViewModel(programDataInteractor)
    }

    @Provides
    internal fun provideSectionsViewModel(programDataInteractor: ProgramDataInteractor): SectionsViewModel {
        return SectionsViewModel(programDataInteractor)
    }
}