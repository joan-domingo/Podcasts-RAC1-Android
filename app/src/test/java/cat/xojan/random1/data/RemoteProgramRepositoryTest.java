package cat.xojan.random1.data;


import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.ProgramData;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RemoteProgramRepositoryTest {

    private RemoteProgramRepository mRemoteRepository;
    private Rac1RetrofitService mService;

    @Before
    public void setUp() {
        mService = mock(Rac1RetrofitService.class);
        mRemoteRepository = new RemoteProgramRepository(mService);
    }

    @Test
    public void get_program_list_successfully() throws IOException {
        when(mService.getProgramData()).thenReturn(Observable.just(getProgramData()));
        TestSubscriber<List<Program>> testSubscriber = new TestSubscriber<>();

        mRemoteRepository.getProgramListObservable().subscribe(testSubscriber);
        testSubscriber.assertValue(getProgramList());
    }

    private ProgramData getProgramData() {
        ProgramData programData = new ProgramData();
        programData.setPrograms(getProgramList());
        return programData;
    }

    private List<Program> getProgramList() {
        List<Program> programs = new ArrayList<>();
        programs.add(new Program("program1", true));
        programs.add(new Program("program2", true));
        programs.add(new Program("program3", true));
        return programs;
    }
}

