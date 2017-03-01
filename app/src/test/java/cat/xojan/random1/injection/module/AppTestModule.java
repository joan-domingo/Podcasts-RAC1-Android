package cat.xojan.random1.injection.module;

import com.crashlytics.android.answers.Answers;

import cat.xojan.random1.Application;
import dagger.Module;

import static org.mockito.Mockito.mock;

@Module
public class AppTestModule extends AppModule {

    public AppTestModule(Application application) {
        super(application);
    }

    @Override
    Answers provideFabricEventLogging() {
        return mock(Answers.class);
    }
}
