package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.Subscription;

public class DownloadsPresenter implements BasePresenter {

    private final ProgramDataInteractor mProgramDataInteractor;
    private Subscription mSubscription;
    private DownloadsUI mListener;

    public interface DownloadsUI {
        /** update recycler view items*/
        void updateRecyclerView(List<Podcast> podcasts);
        /** refresh recycler view*/
        void updateRecyclerView();
    }

    @Inject
    public DownloadsPresenter(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    public void deletePodcast(Podcast podcast) {
        mProgramDataInteractor.deleteDownload(podcast);
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
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public void loadDownloadedPodcasts() {
        mSubscription = mProgramDataInteractor.getDownloadedPodcasts()
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
        mProgramDataInteractor.refreshDownloadedPodcasts();
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
