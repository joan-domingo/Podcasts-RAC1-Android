package cat.xojan.random1.domain;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Environment;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.entities.SectionType;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

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
        when(mProgramRepo.getProgramList()).thenReturn(getDummyProgramList());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertValue(getDummyProgramList());
    }

    @Test
    public void load_programs_successfully_after_first_call() {
        mProgramDataInteractor.setProgramsData(getDummyProgramList());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertValue(getDummyProgramList());
    }

    @Test
    public void fail_to_load_programs() throws IOException {
        mProgramDataInteractor.setProgramsData(null);
        when(mProgramRepo.getProgramList()).thenThrow(new IOException());
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void get_sections_from_program() {
        Program program = getDummyProgram();
        TestObserver<List<Section>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadSections(program).subscribe(testSubscriber);
        testSubscriber.assertValue(getSections());
    }

    @Test
    public void load_podcasts_by_program_successfully() throws IOException {
        Program program = getDummyProgram();
        when(mProgramRepo.getPodcastByProgram(anyString())).thenReturn(Observable.just(getPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPodcasts(program, null, false).subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void load_podcasts_by_section_successfully() throws IOException {
        Program program = getDummyProgram();
        Section section = getSections().get(0);
        when(mProgramRepo.getPodcastBySection(anyString(), anyString())).thenReturn(Observable.just(getPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mProgramDataInteractor.loadPodcasts(program, section, false).subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_podcasts() throws IOException {
        Program program = getDummyProgram();
        when(mProgramRepo.getPodcastByProgram(anyString())).thenThrow(new IOException());
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
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

    private List<Program> getDummyProgramList() {
        List<Program> programs = new ArrayList<>();
        programs.add(new Program("id1", true));
        programs.add(new Program("id2", true));

        return programs;
    }

    private Program getDummyProgram() {
        Program program = new Program("programId", true);
        program.setImageUrl("http://placekitten.com/g/200/300");
        program.setSections(getSections());
        return program;
    }

    private List<Section> getSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("sectionId1", true, SectionType.SECTION));
        sections.add(new Section("sectionId2", true, SectionType.SECTION));
        sections.add(new Section("sectionId3", true, SectionType.SECTION));

        return sections;
    }

    private List<Podcast> getPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "programId1", "title1"));
        podcasts.add(new Podcast("path2", "programId2", "title2"));

        return podcasts;
    }
}
