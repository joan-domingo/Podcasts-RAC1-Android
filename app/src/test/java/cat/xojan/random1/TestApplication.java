package cat.xojan.random1;

import cat.xojan.random1.injection.component.DaggerAppComponent;
import cat.xojan.random1.injection.module.AppTestModule;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppTestModule(this))
                .build();
    }
}
