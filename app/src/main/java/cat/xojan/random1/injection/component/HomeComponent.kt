package cat.xojan.random1.injection.component

import cat.xojan.random1.feature.home.DownloadsFragment
import cat.xojan.random1.feature.home.HomeActivity
import cat.xojan.random1.feature.home.ProgramFragment
import cat.xojan.random1.injection.PerActivity
import cat.xojan.random1.injection.module.BaseActivityModule
import cat.xojan.random1.injection.module.HomeModule
import dagger.Component

@PerActivity
@Component(
        dependencies = [(AppComponent::class)],
        modules = [(BaseActivityModule::class), (HomeModule::class)])
interface HomeComponent : BaseActivityComponent {
    fun inject(homeActivity: HomeActivity)
    fun inject(programFragment: ProgramFragment)
    fun inject(downloadedPodcastFragment: DownloadsFragment)
}