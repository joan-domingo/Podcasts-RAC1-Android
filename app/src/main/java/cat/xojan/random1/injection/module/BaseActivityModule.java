package cat.xojan.random1.injection.module;

import android.app.Activity;
import android.content.Context;

import cat.xojan.random1.injection.PerActivity;
import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class BaseActivityModule {
    private final Activity activity;

    public BaseActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    Activity activity() {
        return activity;
    }

    /**
     * Expose the context to dependents in the graph.
     */
    @Provides
    @PerActivity
    Context context() {
        return activity;
    }
}
