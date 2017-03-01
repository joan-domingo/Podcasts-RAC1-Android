package cat.xojan.random1.injection;

import android.app.DownloadManager;

import com.crashlytics.android.answers.Answers;

import javax.inject.Singleton;

import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.component.AppComponent;
import cat.xojan.random1.receiver.DownloadCompleteReceiver;
import cat.xojan.random1.ui.activity.BaseActivity;
import dagger.Component;

@Singleton
@Component(modules = AppTestModule.class)
public interface AppTestComponent extends AppComponent {
    void inject(BaseActivity baseActivity);
    void inject(DownloadCompleteReceiver downloadCompleteReceiver);

    DownloadManager downloadManager();
    ProgramDataInteractor programDataInteractor();
    Answers answers();
}
