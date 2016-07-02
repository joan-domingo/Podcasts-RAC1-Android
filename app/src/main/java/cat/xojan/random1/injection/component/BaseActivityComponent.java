package cat.xojan.random1.injection.component;

import android.app.Activity;
import android.content.Context;

import cat.xojan.random1.injection.PerActivity;
import cat.xojan.random1.injection.module.BaseActivityModule;
import cat.xojan.random1.ui.BaseActivity;
import dagger.Component;

/**
 * A base component upon which fragment's components may depend.  Activity-level components
 * should extend this component.
 */
@PerActivity // Subtypes of BaseActivityComponent should be decorated with @PerActivity.
@Component(
        dependencies = AppComponent.class,
        modules = BaseActivityModule.class
)
public interface BaseActivityComponent {
    void inject(BaseActivity baseActivity);
    Activity activity(); // Expose the activity to sub-graphs
    Context context();
}
