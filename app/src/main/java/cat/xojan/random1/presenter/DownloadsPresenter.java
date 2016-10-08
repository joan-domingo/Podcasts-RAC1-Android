package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.Subscription;

public class DownloadsPresenter implements BasePresenter {

    private final PodcastDataInteractor mPodcastDataInteractor;
    private Subscription mSubscription;
    private DownloadsUI mListener;

    public interface DownloadsUI {
        /** update recycler view items*/
        void updateRecyclerView(List<Podcast> podcasts);
        /** refresh recycler view*/
        void updateRecyclerView();
    }

    @Inject
    public DownloadsPresenter(PodcastDataInteractor podcastDataInteractor) {
        mPodcastDataInteractor = podcastDataInteractor;
    }

    public void deletePodcast(Podcast podcast) {
        mPodcastDataInteractor.deleteDownload(podcast);
        mListener.updateRecyclerView();
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mSubscription.unsubscribe();
    }

    public void loadDownloadedPodcasts() {
        mSubscription = mPodcastDataInteractor.getDownloadedPodcasts()
                .subscribe(new Subscriber<List<Podcast>>() {
                    @Override
                    public void onCompleted() {
                        // Ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorUtil.logException(e);
                    }

                    @Override
                    public void onNext(List<Podcast> podcasts) {
                        mListener.updateRecyclerView(filterOnlyDownloadedPodcasts(podcasts));
                    }
                });
        mPodcastDataInteractor.refreshDownloadedPodcasts();
    }

    private List<Podcast> filterOnlyDownloadedPodcasts(List<Podcast> podcasts) {
        List<Podcast> filteredPodcasts = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            if (podcast.getState() == Podcast.State.DOWNLOADED) {
                filteredPodcasts.add(podcast);
            }
        }
        Collections.reverse(filteredPodcasts);
        return filteredPodcasts;
    }

    public void setUpUiListener(DownloadsUI listener) {
        mListener = listener;
    }
}
