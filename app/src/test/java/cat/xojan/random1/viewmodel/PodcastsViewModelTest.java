package cat.xojan.random1.viewmodel;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.entities.SectionType;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PodcastsViewModelTest {

    private ProgramDataInteractor mProgramDataInteractor;
    private PodcastsViewModel mViewModel;

    @Before
    public void setUp() {
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mViewModel = new PodcastsViewModel(mProgramDataInteractor);
    }

    @Test
    public void load_downloaded_podcasts_successfully() {
        when(mProgramDataInteractor.getDownloadedPodcasts()).thenReturn(Single.just(getPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_downloaded_podcasts() {
        when(mProgramDataInteractor.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void load_podcasts_successfully() {
        Program program = new Program("program1", true);
        program.setImageUrl("www.image.url");
        Section section = new Section("id1", true, SectionType.SECTION);

        when(mProgramDataInteractor.loadPodcasts(program, section, false)).thenReturn(Flowable.just(getPodcastList()));
        when(mProgramDataInteractor.getDownloadedPodcasts()).thenReturn(Single.just(getDownloadedPodcastList()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mViewModel.loadPodcasts(program, section, false).subscribe(testSubscriber);

        testSubscriber.assertValue(getPodcastList());
    }

    @Test
    public void fail_to_load_podcasts() {
        when(mProgramDataInteractor.getDownloadedPodcasts()).thenReturn(Single.error(new IOException()));
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();

        mViewModel.loadDownloadedPodcasts().subscribe(testSubscriber);
        testSubscriber.assertError(IOException.class);
    }

    @Test
    public void get_downloaded_podcasts_update() {
        PublishSubject<List<Podcast>> ps = PublishSubject.create();

        when(mProgramDataInteractor.getDownloadedPodcastsUpdates()).thenReturn(ps);
        TestObserver<List<Podcast>> testSubscriber = new TestObserver<>();
        mViewModel.getDownloadedPodcastsUpdates().subscribe(testSubscriber);
        ps.onNext(getDownloadedPodcastList());

        testSubscriber.assertValue(getDownloadedPodcastList());
    }

    @Test
    public void set_selected_mode() {
        mViewModel.selectedSection(true);
        verify(mProgramDataInteractor).setSectionSelected(true);
    }

    private List<Podcast> getPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        podcasts.add(new Podcast("path1", "program1", "podcast1"));
        podcasts.add(new Podcast("path2", "program1", "podcast2"));
        podcasts.add(new Podcast("path3", "program1", "podcast3"));
        return podcasts;
    }

    private List<Podcast> getDownloadedPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();

        Podcast podcast1 = new Podcast("path1", "program1", "podcast1");
        podcast1.setFilePath("filePath1");
        podcast1.setState(Podcast.State.DOWNLOADED);

        Podcast podcast3 = new Podcast("path3", "program1", "podcast3");
        podcast3.setFilePath("filePath3");
        podcast3.setState(Podcast.State.DOWNLOADING);

        podcasts.add(podcast1);
        podcasts.add(podcast3);

        return podcasts;
    }
}
