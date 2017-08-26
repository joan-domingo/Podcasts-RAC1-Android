package cat.xojan.random1.data;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.PodcastData;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.ProgramData;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Test @Ignore
    public void get_program_list() throws IOException {
        when(mService.getProgramData().execute().body()).thenReturn(getProgramData());

        assertEquals(mRemoteRepository.getProgramList(), getProgramList());
    }

    @Test
    public void get_podcasts_list_by_program() throws IOException {
        when(mService.getPodcastData(anyString())).thenReturn(Observable.just(getPodcastData()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mRemoteRepository.getPodcastByProgram("programId").subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void get_podcasts_list_by_section() throws IOException {
        when(mService.getPodcastData(anyString(), anyString())).thenReturn(Observable.just(getPodcastData()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mRemoteRepository.getPodcastBySection("programId", "sectionId").subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
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

    private PodcastData getPodcastData() {
        PodcastData podcastData = new PodcastData();
        podcastData.setPodcasts(getPodcastList());
        return podcastData;
    }

    private List<Podcast> getPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "program1", "title1"));
        podcasts.add(new Podcast("path2", "program1", "title2"));
        podcasts.add(new Podcast("path3", "program1", "title3"));
        return podcasts;
    }
}

