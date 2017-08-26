package cat.xojan.random1.viewmodel;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProgramsViewModelTest {

    private ProgramDataInteractor mProgramDataInteractor;
    private ProgramsViewModel mViewModel;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mViewModel = new ProgramsViewModel(mProgramDataInteractor);
    }

    @Test
    public void load_programs_successfully() {
        when(mProgramDataInteractor.loadPrograms()).thenReturn(Observable.just(getDummyProgramList()));
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();

        mViewModel.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertValue(getDummyProgramListResult());
    }

    @Test
    public void fail_to_load_programs() {
        when(mProgramDataInteractor.loadPrograms()).thenReturn(Observable.error(new IOException()));
        TestObserver<List<Program>> testSubscriber = new TestObserver<>();

        mViewModel.loadPrograms().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    private List<Program> getDummyProgramList() {
        List<Program> programs = new ArrayList<>();
        programs.add(new Program("program1", true));
        programs.add(new Program("program2", false));
        programs.add(new Program("program3", true));
        return programs;
    }

    private List<Program> getDummyProgramListResult() {
        List<Program> programs = new ArrayList<>();
        programs.add(new Program("program1", true));
        programs.add(new Program("program3", true));
        return programs;
    }
}
