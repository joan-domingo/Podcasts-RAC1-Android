package cat.xojan.random1.injection.module

import android.app.Activity
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class BaseActivityModule(private val activity: Activity) {

    @Provides
    internal fun activity(): Activity {
        return activity
    }

    @Provides
    internal fun context(): Context {
        return activity
    }
}