package cat.xojan.random1.injection.component;

import android.app.DownloadManager;

import javax.inject.Singleton;

import cat.xojan.random1.domain.entities.CrashReporter;
import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.module.AppModule;
import cat.xojan.random1.receiver.DownloadCompleteReceiver;
import cat.xojan.random1.ui.activity.BaseActivity;
import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(DownloadCompleteReceiver downloadCompleteReceiver);

    DownloadManager downloadManager();
    ProgramDataInteractor programDataInteractor();
    EventLogger eventLogger();
    CrashReporter crashReporter();
}
