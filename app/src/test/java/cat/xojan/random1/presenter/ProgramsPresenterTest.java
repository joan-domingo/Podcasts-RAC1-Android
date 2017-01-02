package cat.xojan.random1.presenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProgramsPresenterTest {

    private ProgramDataInteractor mProgramDataInteractor;
    private ProgramsPresenter mPresenter;
    private ProgramsPresenter.ProgramListener mMockListener;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mPresenter = new ProgramsPresenter(mProgramDataInteractor, Schedulers.immediate(),
                Schedulers.immediate());
        mMockListener = mock(ProgramsPresenter.ProgramListener.class);
    }

    @Test
    public void show_programs_successfully() {
        // Given a program
        when(mProgramDataInteractor.loadPrograms())
                .thenReturn(Observable.from(getDummyPrograms()));

        // When we load the sections
        mPresenter.showPrograms(mMockListener);

        // Then we get the sections
        verify(mMockListener).onProgramsLoaded(getDummyProgramsResult());
    }

    @After
    public void tearDown() {
        mPresenter.destroy();
    }

    private List<Program> getDummyPrograms() {
        List<Program> programs = new ArrayList<>();

        Program program1 = new Program("program1");
        program1.setIsActive(true);

        Program program2 = new Program("program2");
        program2.setIsActive(true);

        Program program3 = new Program("program3");
        program3.setIsActive(false);

        programs.add(program1);
        programs.add(program2);
        programs.add(program3);

        return programs;
    }

    private List<Program> getDummyProgramsResult() {
        List<Program> programs = new ArrayList<>();

        Program program1 = new Program("program1");
        Program program2 = new Program("program2");

        programs.add(program1);
        programs.add(program2);

        return programs;
    }
}
