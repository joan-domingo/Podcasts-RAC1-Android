package cat.xojan.random1.injection.component

import cat.xojan.random1.feature.mediaplayback.MediaPlaybackFullScreenActivity
import cat.xojan.random1.injection.PerActivity
import cat.xojan.random1.injection.module.BaseActivityModule
import dagger.Component

@PerActivity
@Component(
        dependencies = [(AppComponent::class)],
        modules = [(BaseActivityModule::class)])
interface MediaPlaybackFullScreenComponent : BaseActivityComponent {
    fun inject(mediaPlaybackFullScreenActivity: MediaPlaybackFullScreenActivity)
}