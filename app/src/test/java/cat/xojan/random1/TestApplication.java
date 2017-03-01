package cat.xojan.random1;

import cat.xojan.random1.injection.AppTestComponent;
import cat.xojan.random1.injection.AppTestModule;
import cat.xojan.random1.injection.component.AppComponent;
import cat.xojan.random1.injection.DaggerAppTestComponent;

public class TestApplication extends Application {

    private AppTestComponent mAppTestComponent;

    @Override
    public void onCreate() {
        mAppTestComponent = DaggerAppTestComponent.builder()
                .appTestModule(new AppTestModule(this))
                .build();
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppTestComponent;
    }
}
