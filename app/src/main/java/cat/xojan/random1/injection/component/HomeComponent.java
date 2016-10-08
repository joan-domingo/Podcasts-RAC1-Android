package cat.xojan.random1.injection.component;

import cat.xojan.random1.injection.PerActivity;
import cat.xojan.random1.injection.module.BaseActivityModule;
import cat.xojan.random1.injection.module.HomeModule;
import cat.xojan.random1.ui.activity.HomeActivity;
import cat.xojan.random1.ui.fragment.DownloadsFragment;
import cat.xojan.random1.ui.fragment.PodcastListFragment;
import cat.xojan.random1.ui.fragment.ProgramFragment;
import dagger.Component;

@PerActivity
@Component(
        dependencies = AppComponent.class,
        modules = {
                BaseActivityModule.class,
                HomeModule.class
        }
)
public interface HomeComponent extends BaseActivityComponent {
    void inject(HomeActivity homeActivity);
    void inject(PodcastListFragment podcastListFragment);
    void inject(ProgramFragment programFragment);
    void inject(DownloadsFragment downloadedPodcastFragment);
}
