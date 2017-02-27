package cat.xojan.random1.viewmodel;

import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PodcastViewModelTest {

    private Context mContext;
    private Podcast mPodcast;
    private PodcastViewModel mViewModel;
    private ProgramDataInteractor mProgramDataInteractor;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mPodcast = new Podcast("path1", "id1", "title1");
        mProgramDataInteractor = mock(ProgramDataInteractor.class);

        mViewModel = new PodcastViewModel(mContext, mPodcast, mProgramDataInteractor);
    }

    @Test
    public void read_title() {
        assertEquals(mViewModel.getTitle(), mPodcast.getTitle());
    }

    @Test
    public void read_image_url() {
        assertEquals(mViewModel.getImageUrl(), mPodcast.getImageUrl());
    }

    @Test
    public void read_state() {
        assertEquals(mViewModel.getState(), mPodcast.getState());
    }

    @Test
    public void click_podcast() {
        mViewModel.onClickPodcast().onClick(any());
        verify(mContext).startActivity(any());
    }

    @Test
    public void click_podcast_with_icon_downloading() {
        mPodcast.setState(Podcast.State.DOWNLOADING);
        mViewModel.onClickIcon().onClick(new View(mContext));
        verify(mProgramDataInteractor).refreshDownloadedPodcasts();
    }

    @Test
    public void click_podcast_with_icon_downloaded() {
        mPodcast.setState(Podcast.State.DOWNLOADED);
        mViewModel.onClickIcon().onClick(new View(mContext));
        verify(mProgramDataInteractor).deleteDownload(mPodcast);
        verify(mProgramDataInteractor).refreshDownloadedPodcasts();
    }

    @Test
    public void click_podcast_with_icon_loaded() {
        mPodcast.setState(Podcast.State.LOADED);
        mViewModel.onClickIcon().onClick(new View(mContext));
        verify(mProgramDataInteractor).download(mPodcast);
        verify(mProgramDataInteractor).refreshDownloadedPodcasts();
    }
}
