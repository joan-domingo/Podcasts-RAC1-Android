package cat.xojan.random1.injection.component

import android.app.Activity
import android.content.Context
import cat.xojan.random1.feature.BaseActivity
import cat.xojan.random1.injection.PerActivity
import cat.xojan.random1.injection.module.BaseActivityModule
import dagger.Component

@PerActivity // Subtypes of BaseActivityComponent should be decorated with @PerActivity.
@Component(dependencies = [(AppComponent::class)], modules = [(BaseActivityModule::class)])
interface BaseActivityComponent {
    fun inject(baseActivity: BaseActivity)
    fun activity(): Activity  // Expose the activity to sub-graphs
    fun context(): Context
}