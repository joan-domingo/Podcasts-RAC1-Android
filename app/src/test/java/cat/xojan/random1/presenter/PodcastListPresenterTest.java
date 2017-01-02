package cat.xojan.random1.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PodcastListPresenterTest {

    private Context mContext;
    private ProgramDataInteractor mProgramDataInteractor;
    private DownloadManager mDownloadManager;
    private PodcastListPresenter mPresenter;
    private PodcastListPresenter.PodcastsListener mMockUiListener;

    @Before
    public void setUp() {
        mMockUiListener = mock(PodcastListPresenter.PodcastsListener.class);
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mContext = mock(Context.class);
        mDownloadManager = mock(DownloadManager.class);
        mPresenter = new PodcastListPresenter(mProgramDataInteractor, mContext, mDownloadManager,
                Schedulers.immediate(), Schedulers.immediate());
        mPresenter.setPodcastsListener(mMockUiListener);
    }

    @Test
    public void load_podcasts_succesfully() {
        // When we load the podcasts
        when(mProgramDataInteractor.loadPodcastsByProgram(null, null, false))
                .thenReturn(Observable.from(getDummyPodcastList()));
        mPresenter.loadPodcasts(new Bundle(), false);

        // Then the recycler view is updated
        mMockUiListener.updateRecyclerView(getDummyPodcastList());
    }

    @Test
    public void fail_to_load_podcasts() {
        // When we load the podcasts
        when(mProgramDataInteractor.loadPodcastsByProgram(null, null,
                false)).thenReturn(Observable.<Podcast>error(new IOException()));
        mPresenter.loadPodcasts(new Bundle(), false);

        // Then the recycler view is updated
        mMockUiListener.updateRecyclerView(Collections.<Podcast>emptyList());
    }

    @Test @Ignore
    public void should_download_succesfully() {
        // Given a podcast
        Podcast podcast = new Podcast("path1", "program1", "title1");
        podcast.setAudioId("audioId1");

        // When we download it
        mPresenter.download(podcast);

        // Then
        verify(mProgramDataInteractor).refreshDownloadedPodcasts();
    }

    @Test
    public void refresh_downloaded_podcasts_succesfully() {
        // When we load the podcasts
        when(mProgramDataInteractor.getDownloadedPodcasts())
                .thenReturn(Observable.just(getDummyPodcastList()));
        mPresenter.resume();

        // Then the recycler view is updated
        mMockUiListener.updateRecyclerView(getDummyPodcastList());
    }

    @Test
    public void fail_to_refresh_downloaded_podcasts() {
        when(mProgramDataInteractor.getDownloadedPodcasts())
                .thenReturn(Observable.<List<Podcast>>error(new IOException()));
        mPresenter.resume();
    }

    @After
    public void tearDown() {
        mPresenter.destroy();
    }

    private List<Podcast> getDummyPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "program1", "title1"));
        podcasts.add(new Podcast("path2", "program2", "title2"));
        podcasts.add(new Podcast("path3", "program3", "title3"));
        return podcasts;
    }
}
