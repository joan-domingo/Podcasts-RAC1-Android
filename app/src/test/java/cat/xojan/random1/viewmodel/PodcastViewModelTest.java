package cat.xojan.random1.viewmodel;

import android.app.DownloadManager;
import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PodcastViewModelTest {

    private Context mContext;
    private Podcast mPodcast;
    private PodcastViewModel mViewModel;
    private ProgramDataInteractor mProgramDataInteractor;
    private DownloadManager mDownloadManager;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mPodcast = new Podcast("path1", "id1", "title1");
        mViewModel = new PodcastViewModel(mContext, mPodcast, mProgramDataInteractor,
                mDownloadManager);
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
}
