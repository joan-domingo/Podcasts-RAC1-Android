package cat.xojan.random1.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SectionPresenterTest {

    private ProgramDataInteractor mProgramDataInteractor;
    private SectionPresenter mPresenter;
    private SectionPresenter.SectionListUi mMockUiListener;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mPresenter = new SectionPresenter(mProgramDataInteractor, Schedulers.immediate(),
                Schedulers.immediate());
        mMockUiListener = mock(SectionPresenter.SectionListUi.class);
        mPresenter.setListener(mMockUiListener);
    }

    @Test
    public void load_sections_success() {
        // Given a program
        when(mProgramDataInteractor.loadSections(any(Program.class)))
                .thenReturn(Observable.from(getDummySections()));

        // When we load the sections
        mPresenter.loadSections(new Program("program1"));

        // Then we get the sections
        verify(mMockUiListener).updateRecyclerView(getDummySectionsResult());
    }

    @After
    public void tearDown() {
        mPresenter.destroy();
    }

    private List<Section> getDummySections() {
        List<Section> sections = new ArrayList<>();

        Section section1 = new Section("section1");
        section1.setIsActive(true);
        sections.add(section1);

        sections.add(new Section("section2"));

        Section section3 = new Section("section3");
        section3.setIsActive(true);
        sections.add(section3);

        return sections;
    }

    private List<Section> getDummySectionsResult() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("section1"));
        sections.add(new Section("section3"));
        return sections;
    }
}
