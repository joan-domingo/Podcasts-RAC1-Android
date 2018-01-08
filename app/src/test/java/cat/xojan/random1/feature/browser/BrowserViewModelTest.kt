package cat.xojan.random1.feature.browser

import org.junit.Before

import java.util.ArrayList

import cat.xojan.random1.domain.interactor.PodcastDataInteractor
import cat.xojan.random1.domain.interactor.ProgramDataInteractor
import cat.xojan.random1.domain.model.Podcast

import cat.xojan.random1.testutil.podcast1
import cat.xojan.random1.testutil.podcast3
import org.mockito.Mockito.mock

class BrowserViewModelTest {

    private var programDataInteractor: ProgramDataInteractor? = null
    private var podcastDataInteractor: PodcastDataInteractor? = null
    private var mViewModel: BrowserViewModel? = null

    /*@Test
    public void load_downloaded_podcasts_successfully() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.just(getPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        viewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_downloaded_podcasts() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        viewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void load_podcasts_successfully() {
        Program program = getProgram1();
        Section section = getSection1();

        when(programDataIntera.loadPodcasts(program, section, false)).thenReturn(Flowable.just(getPodcastList()));
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.just(getDownloadedPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        viewModel.loadPodcasts(program, section, false).subscribe(testSubscriber);

        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_podcasts() {
        when(programDataIntera.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        viewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void get_downloaded_podcasts_update() {
        PublishSubject<List<Podcast>> ps = PublishSubject.create();

        when(programDataIntera.getDownloadedPodcastsUpdates()).thenReturn(ps);
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        viewModel.getDownloadedPodcastsUpdates().subscribe(testSubscriber);
        ps.onNext(getDownloadedPodcastList());

        testSubscriber.assertValue(getDownloadedPodcastList());
    }

    @Test
    public void set_selected_mode() {
        viewModel.selectedSection(true);
        verify(programDataIntera).setSectionSelected(true);
    }*/

    private val downloadedPodcastList: List<Podcast>
        get() {
            val podcasts = ArrayList<Podcast>()

            podcasts.add(podcast1)
            podcasts.add(podcast3)

            return podcasts
        }

    @Before
    fun setUp() {
        programDataInteractor = mock(ProgramDataInteractor::class.java)
        podcastDataInteractor = mock(PodcastDataInteractor::class.java)
        mViewModel = BrowserViewModel(podcastDataInteractor!!, programDataInteractor!!)
    }
}
