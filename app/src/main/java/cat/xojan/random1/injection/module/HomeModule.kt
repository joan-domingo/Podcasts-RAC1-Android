package cat.xojan.random1.injection.module

import android.app.Activity
import cat.xojan.random1.domain.interactor.PodcastDataInteractor
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.feature.browser.BrowserViewModel
import cat.xojan.random1.feature.home.HomeViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule(private val activity: Activity) {

    @Provides
    fun provideBrowserViewModel(podcastInteractor: PodcastDataInteractor,
                                programInteractor: ProgramDataInteractor): BrowserViewModel {
        return BrowserViewModel(podcastInteractor, programInteractor)
    }

    @Provides
    fun provideHomeViewModel(podcastInteractor: PodcastDataInteractor): HomeViewModel {
        return HomeViewModel(podcastInteractor)
    }
}