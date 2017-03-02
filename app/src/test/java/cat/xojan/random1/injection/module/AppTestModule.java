package cat.xojan.random1.injection.module;

import android.app.DownloadManager;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.Application;
import cat.xojan.random1.data.Rac1RetrofitService;
import cat.xojan.random1.domain.entities.CrashReporter;
import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import dagger.Module;
import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Module
public class AppTestModule extends AppModule {

    public AppTestModule(Application application) {
        super(application);
    }

    @Override
    EventLogger provideEventLogger() {
        return mock(EventLogger.class);
    }

    @Override
    CrashReporter provideCrashReporter() {
        return mock(CrashReporter.class);
    }

    @Override
    ProgramDataInteractor provideProgramDataInteractor(Rac1RetrofitService service,
                                                       DownloadManager downloadManager) {
        ProgramDataInteractor interactor = mock(ProgramDataInteractor.class);
        when(interactor.loadPrograms()).thenReturn(Observable.just(getProgramList()));
        when(interactor.loadPodcasts(any(Program.class), any(Section.class), anyBoolean())).thenReturn(Observable.just(getPodcastList()));
        when(interactor.getDownloadedPodcastsUpdates()).thenReturn(Observable.just(getPodcastList()));
        when(interactor.getDownloadedPodcasts()).thenReturn(Observable.just(getPodcastList()));
        return interactor;
    }

    private List<Podcast> getPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        Podcast podcast1 = new Podcast("path1", "program1", "Program title 1");
        Podcast podcast2 = new Podcast("path2", "program1", "Program title 2");

        podcasts.add(podcast1);
        podcasts.add(podcast2);
        return podcasts;
    }

    private List<Program> getProgramList() {
        List<Program> programs = new ArrayList<>();

        Program program1 = new Program("program1", true);
        program1.setTitle("Program 1");
        program1.setImageUrl("www.image.url");

        Program program2 = new Program("program2", true);
        program2.setTitle("Program 2");
        program2.setImageUrl("www.image.url");

        programs.add(program1);
        programs.add(program2);

        return programs;
    }
}
