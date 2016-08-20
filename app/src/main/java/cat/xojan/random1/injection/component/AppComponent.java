package cat.xojan.random1.injection.component;

import javax.inject.Singleton;

import cat.xojan.random1.injection.module.AppModule;
import cat.xojan.random1.ui.BaseActivity;
import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseActivity baseActivity);

    //Exposed to sub-graphs.
}
