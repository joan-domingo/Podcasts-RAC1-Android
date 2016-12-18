package cat.xojan.random1.domain;

import android.content.Context;
import android.os.Environment;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.repository.ProgramRepository;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramDataInteractorTest {

    private ProgramRepository mProgramRepo;
    private PreferencesDownloadPodcastRepository mDownloadsRepo;
    private ProgramDataInteractor mProgramDataInteractor;
    private Context mMockContext;

    @Before
    public void setUp() {
        mProgramRepo = mock(ProgramRepository.class);
        mDownloadsRepo = mock(PreferencesDownloadPodcastRepository.class);
        mMockContext = mock(Context.class);
        mProgramDataInteractor = new ProgramDataInteractor(mProgramRepo, mDownloadsRepo,
                mMockContext);
    }

    @Test
    public void should_load_programs_success() throws Exception {
        // Given an uninitialized program data
        mProgramDataInteractor.setProgramsData(null);

        // When we load the programs for the first time
        when(mProgramRepo.getProgramList()).thenReturn(getDummyProgramList());
        mProgramDataInteractor.loadPrograms().toBlocking().first();

        // Then the program list is initialized
        assertEquals(mProgramDataInteractor.getProgramData(), getDummyProgramList());
    }

    @Test
    public void should_load_programs_fail() throws Exception {
        // Given an uninitialized program data
        mProgramDataInteractor.setProgramsData(null);

        // When we load the program data as null
        when(mProgramRepo.getProgramList()).thenReturn(null);
        TestSubscriber<Program> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.loadPrograms().subscribe(testSubscriber);

        // Then we catch an error
        testSubscriber.assertError(NullPointerException.class);
    }

    @Test
    public void should_load_sections_success() throws Exception {
        // Given a dummy program
        Program program = getDummyProgram();

        // When we load its sections
        List<Section> sections = mProgramDataInteractor.loadSections(program)
                .toList().toBlocking().first();

        // Then we get the expected sections
        assertEquals(sections, getDummySectionsResult());
    }

    @Test
    public void should_load_sections_fail() throws Exception {
        // Given a dummy program with sections as null
        Program program = getDummyProgram();
        program.setSections(null);

        // When we load its sections
        TestSubscriber<Section> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.loadSections(program).subscribe(testSubscriber);

        // Then we catch an error
        testSubscriber.assertError(NullPointerException.class);
    }

    @Test
    public void should_load_podcasts_by_program_success() throws Exception {
        // Given a dummy program
        Program program = getDummyProgram();

        // When we ask for the podcasts of that program
        when(mProgramRepo.getPodcastByProgram(anyString())).thenReturn(getDummyPodcastList());
        List<Podcast> podcasts = mProgramDataInteractor.loadPodcastsByProgram(program, null, false)
                .toList().toBlocking().first();

        // Then we get the podcasts
        assertEquals(podcasts, getDummyPodcastList());

        // And they contain an image url
        assertNotNull(podcasts.get(0).getImageUrl());
        assertNotNull(podcasts.get(1).getImageUrl());
    }

    @Test
    public void should_load_podcasts_by_section_success() throws Exception {
        // Given a dummy program and a dummy section
        Program program = getDummyProgram();
        Section section = getDummySections().get(0);

        // When we ask for the podcasts of that section
        when(mProgramRepo.getPodcastBySection(anyString(), anyString()))
                .thenReturn(getDummyPodcastList());
        List<Podcast> podcasts = mProgramDataInteractor
                .loadPodcastsByProgram(program, section, false)
                .toList().toBlocking().first();

        // Then we get the podcasts
        assertEquals(podcasts, getDummyPodcastList());

        // And they contain an image url
        assertNotNull(podcasts.get(0).getImageUrl());
        assertNotNull(podcasts.get(1).getImageUrl());
    }

    @Test
    public void add_downloaded_podcast_and_refresh_list_fail() {
        // Given a subscription to the downloads publisher
        TestSubscriber<List<Podcast>> testSubscriber = new TestSubscriber<>();
        mProgramDataInteractor.getDownloadedPodcasts().subscribe(testSubscriber);

        // When we set a podcast as "downloaded"
        when(mMockContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS))
                .thenReturn(new File("downloads/"));
        when(mMockContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS))
                .thenReturn(new File("podcasts/"));
        mProgramDataInteractor.addDownload("audioId1");

        // Then
        testSubscriber.assertValue(new ArrayList<Podcast>());
    }

    private List<Program> getDummyProgramList() {
        List<Program> programs = new ArrayList<>();
        programs.add(new Program("id1"));
        programs.add(new Program("id2"));

        return programs;
    }

    private Program getDummyProgram() {
        Program program = new Program("programId");
        program.setImageUrl("http://placekitten.com/g/200/300");
        program.setSections(getDummySections());

        return program;
    }

    private List<Section> getDummySections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("sectionId1"));
        sections.add(new Section("sectionId2"));
        sections.add(new Section("sectionId3"));

        return sections;
    }

    private List<Section> getDummySectionsResult() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("sectionId2"));
        sections.add(new Section("sectionId3"));

        return sections;
    }

    private List<Podcast> getDummyPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "programId1", "title1"));
        podcasts.add(new Podcast("path2", "programId2", "title2"));

        return podcasts;
    }
}
