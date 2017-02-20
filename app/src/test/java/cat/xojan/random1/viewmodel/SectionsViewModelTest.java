package cat.xojan.random1.viewmodel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SectionsViewModelTest {

    private ProgramDataInteractor mProgramDataInteractor;
    private SectionsViewModel mViewModel;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mViewModel = new SectionsViewModel(mProgramDataInteractor);
    }

    @Test
    public void load_sections_successfully() {
        Program program = new Program("program1", true);
        program.setImageUrl("www.image.url");

        when(mProgramDataInteractor.loadSections(any(Program.class))).thenReturn(Observable.just(getSections()));
        TestSubscriber<List<Section>> testSubscriber = new TestSubscriber<>();
        mViewModel.loadSections(program).subscribe(testSubscriber);

        testSubscriber.assertValue(getSectionsResult());
    }

    private List<Section> getSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("section1", true, Section.Type.SECTION));
        sections.add(new Section("section2", false, Section.Type.SECTION));
        sections.add(new Section("section3", true, Section.Type.GENERIC));
        sections.add(new Section("section4", true, Section.Type.SECTION));
        return sections;
    }

    private List<Section> getSectionsResult() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("section1", true, Section.Type.SECTION));
        sections.add(new Section("section4", true, Section.Type.SECTION));
        return sections;
    }
}
