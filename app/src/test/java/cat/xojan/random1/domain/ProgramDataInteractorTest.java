package cat.xojan.random1.domain;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.repository.ProgramRepository;
import rx.observers.TestSubscriber;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramDataInteractorTest {

    private ProgramRepository mProgramRepo;
    private PreferencesDownloadPodcastRepository mDownloadsRepo;
    private ProgramDataInteractor mProgramDataInteractor;

    @Before
    public void setUp() {
        mProgramRepo = mock(ProgramRepository.class);
        mDownloadsRepo = mock(PreferencesDownloadPodcastRepository.class);
        mProgramDataInteractor = new ProgramDataInteractor(mProgramRepo, mDownloadsRepo,
                mock(Context.class));
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
}
