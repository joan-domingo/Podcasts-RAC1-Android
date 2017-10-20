package cat.xojan.random1.domain;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.repository.ProgramRepository;
import io.reactivex.Flowable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

import static cat.xojan.random1.testutil.DataKt.getPodcastList;
import static cat.xojan.random1.testutil.DataKt.getProgram1;
import static cat.xojan.random1.testutil.DataKt.getProgramList;
import static cat.xojan.random1.testutil.DataKt.getSectionList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramDataInteractorTest {

    private ProgramRepository mProgramRepo;
    private PreferencesDownloadPodcastRepository mDownloadsRepo;
    private ProgramDataInteractor mProgramDataInteractor;
    private Context mMockContext;
    private DownloadManager mDownloadManager;
    private EventLogger mEventLogger;

    @Before
    public void setUp() {
        mProgramRepo = mock(ProgramRepository.class);
        mDownloadsRepo = mock(PreferencesDownloadPodcastRepository.class);
        mMockContext = mock(Context.class);
        mDownloadManager = mock(DownloadManager.class);
        mEventLogger = mock(EventLogger.class);

        mProgramDataInteractor = new ProgramDataInteractor(mProgramRepo, mDownloadsRepo,
                mMockContext, mDownloadManager, mEventLogger);
    }

    @Test
    public void load_programs_successfully_during_first_call() throws IOException {
        mProgramDataInteractor.setProgramsData(null);
        when(mProgramRepo.getPrograms()).thenReturn(getProgramList());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertValue(getProgramList());
    }

    @Test
    public void load_programs_successfully_after_first_call() {
        mProgramDataInteractor.setProgramsData(getProgramList());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertValue(getProgramList());
    }

    @Test
    public void fail_to_load_programs() throws IOException {
        mProgramDataInteractor.setProgramsData(null);
        when(mProgramRepo.getPrograms()).thenThrow(new IOException());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void get_sections_from_program() {
        Program program = getDummyProgram();
        TestObserver<List<Section>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadSections(program).subscribe(testSubscriber);
        testSubscriber.assertValue(getSectionList());
    }

    @Test @Ignore
    public void load_podcasts_by_program_successfully() throws IOException {
        Program program = getDummyProgram();
        when(mProgramRepo.getPodcast(anyString(), null)).thenReturn(Flowable.just(getPodcastList()));
        TestSubscriber<List<Podcast>> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.loadPodcasts(program, null, false).subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void load_podcasts_by_section_successfully() throws IOException {
        Program program = getDummyProgram();
        Section section = getSectionList().get(0);
        when(mProgramRepo.getPodcast(anyString(), anyString())).thenReturn(Flowable.just(getPodcastList()));
        TestSubscriber<List<Podcast>> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.loadPodcasts(program, section, false).subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test @Ignore
    public void fail_to_load_podcasts() throws IOException {
        Program program = getDummyProgram();
        when(mProgramRepo.getPodcast(anyString(), null)).thenThrow(new IOException());
        TestSubscriber<List<Podcast>> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.loadPodcasts(program, null, false).subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void add_downloaded_podcast_and_refresh_list_fail() {
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.getDownloadedPodcasts().subscribe(testSubscriber);

        when(mMockContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).thenReturn(new File("downloads/"));
        when(mMockContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)).thenReturn(new File("podcasts/"));
        mProgramDataInteractor.addDownload("audioId1");

        testSubscriber.assertValue(new ArrayList<Podcast>());
    }

    private Program getDummyProgram() {
        Program program = getProgram1();
        program.setSections(getSectionList());
        return program;
    }
}
