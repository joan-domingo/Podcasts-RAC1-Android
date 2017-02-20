package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;

public class DownloadsPresenterTest {

    /*private ProgramDataInteractor mProgramDataInteractor;
    private DownloadsPresenter mPresenter;
    private DownloadsPresenter.DownloadsUI mMockUiListener;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mPresenter = new DownloadsPresenter(mProgramDataInteractor);
        mMockUiListener = mock(DownloadsPresenter.DownloadsUI.class);
        mPresenter.setUpUiListener(mMockUiListener);
    }

    @After
    public void tearDown() {
        mPresenter.destroy();
    }

    @Test
    public void load_downloaded_podcasts_success() {
        //Given a list of podcasts
        when(mProgramDataInteractor.getDownloadedPodcasts())
                .thenReturn(Observable.just(getDummyPodcastList()));

        // When we load the downloaded podcasts
        mPresenter.loadDownloadedPodcasts();

        // Then we only get the downloaded ones
        verify(mMockUiListener).updateRecyclerView(getDummyPodcastListResult());
    }

    @Test
    public void load_downloaded_podcasts_fail() {
        //Given a list of podcasts
        when(mProgramDataInteractor.getDownloadedPodcasts())
                .thenReturn(Observable.just(getDummyPodcastList()));

        // When we load the downloaded podcasts
        mPresenter.setUpUiListener(null);
        mPresenter.loadDownloadedPodcasts();

        // Then we caught an exception
    }

    @Test
    public void should_delete_podcasts() {
        // Given a podcast
        Podcast podcast = new Podcast("path", "programId", "title");

        // When we delete it
        mPresenter.deletePodcast(podcast);

        // Then the podcasts is deleted and the recycler view is updated
        verify(mProgramDataInteractor).deleteDownload(podcast);
        mMockUiListener.updateRecyclerView();
    }*/

    private List<Podcast> getDummyPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();

        Podcast podcast1 = new Podcast("path1", "programId1", "title1");
        podcast1.setState(Podcast.State.DOWNLOADING);

        Podcast podcast2 = new Podcast("path2", "programId2", "title2");
        podcast2.setState(Podcast.State.DOWNLOADED);

        Podcast podcast3 = new Podcast("path3", "programId3", "title3");
        podcast3.setState(Podcast.State.LOADED);

        Podcast podcast4 = new Podcast("path4", "programId4", "title4");
        podcast4.setState(Podcast.State.DOWNLOADED);

        podcasts.add(podcast1);
        podcasts.add(podcast2);
        podcasts.add(podcast3);
        podcasts.add(podcast4);

        return podcasts;
    }

    private List<Podcast> getDummyPodcastListResult() {
        List<Podcast> podcasts = new ArrayList<>();

        Podcast podcast2 = new Podcast("path2", "programId2", "title2");
        podcast2.setState(Podcast.State.DOWNLOADED);

        Podcast podcast4 = new Podcast("path4", "programId4", "title4");
        podcast4.setState(Podcast.State.DOWNLOADED);

        podcasts.add(podcast4);
        podcasts.add(podcast2);

        return podcasts;
    }
}
