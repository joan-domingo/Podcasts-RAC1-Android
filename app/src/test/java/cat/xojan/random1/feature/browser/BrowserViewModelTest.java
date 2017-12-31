package cat.xojan.random1.feature.browser;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.model.Podcast;

import static cat.xojan.random1.testutil.DataKt.getPodcast1;
import static cat.xojan.random1.testutil.DataKt.getPodcast3;
import static org.mockito.Mockito.mock;

public class BrowserViewModelTest {

    private ProgramDataInteractor programDataInteractor;
    private PodcastDataInteractor podcastDataInteractor;
    private BrowserViewModel mViewModel;

    @Before
    public void setUp() {
        programDataInteractor = mock(ProgramDataInteractor.class);
        podcastDataInteractor = mock(PodcastDataInteractor.class);
        mViewModel = new BrowserViewModel(podcastDataInteractor, programDataInteractor);
    }

    /*@Test
    public void load_downloaded_podcasts_successfully() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.just(getPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_downloaded_podcasts() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void load_podcasts_successfully() {
        Program program = getProgram1();
        Section section = getSection1();

        when(programDataIntera.loadPodcasts(program, section, false)).thenReturn(Flowable.just(getPodcastList()));
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.just(getDownloadedPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mViewModel.loadPodcasts(program, section, false).subscribe(testSubscriber);

        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_podcasts() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void get_downloaded_podcasts_update() {
        PublishSubject<List<Podcast>> ps = PublishSubject.create();

        when(programDataIntera.getDownloadedPodcastsUpdates()).thenReturn(ps);
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mViewModel.getDownloadedPodcastsUpdates().subscribe(testSubscriber);
        ps.onNext(getDownloadedPodcastList());

        testSubscriber.assertValue(getDownloadedPodcastList());
    }

    @Test
    public void set_selected_mode() {
        mViewModel.selectedSection(true);
        verify(programDataIntera).setSectionSelected(true);
    }*/

    private List<Podcast> getDownloadedPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();

        podcasts.add(getPodcast1());
        podcasts.add(getPodcast3());

        return podcasts;
    }
}
