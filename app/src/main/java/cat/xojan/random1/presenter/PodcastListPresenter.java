package cat.xojan.random1.presenter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.BasePresenter;
import cat.xojan.random1.ui.fragment.PodcastListFragment;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;

public class PodcastListPresenter implements BasePresenter {

    private final DownloadManager mDownloadManager;
    private final Scheduler mIoScheduler;
    private final Scheduler mMainThreadScheduler;
    private ProgramDataInteractor mProgramDataInteractor;
    private Context mContext;
    private Subscription mLoadedPodcastSubscription;
    private PodcastsListener mListener;
    private Subscription mDownloadedPodcastSubscription;

    public interface PodcastsListener {
        /** update podcast list*/
        void updateRecyclerView(List<Podcast> podcasts);
        /** update podcasts with downloaded*/
        void updateRecyclerViewWithDownloaded(List<Podcast> podcasts);
    }

    @Inject
    public PodcastListPresenter(ProgramDataInteractor programDataInteractor, Context context,
                                DownloadManager downloadManager, Scheduler ioScheduler,
                                Scheduler mainScheduler) {
        mProgramDataInteractor = programDataInteractor;
        mContext = context;
        mDownloadManager = downloadManager;
        mIoScheduler = ioScheduler;
        mMainThreadScheduler = mainScheduler;
    }

    public void setPodcastsListener(PodcastsListener listener) {
        mListener = listener;
    }

    public void loadPodcasts(Bundle args, boolean refresh) {
        Program program = (Program) args.get(PodcastListFragment.ARG_PROGRAM);
        Section section = (Section) args.get(PodcastListFragment.ARG_SECTION);

        mLoadedPodcastSubscription =
                mProgramDataInteractor.loadPodcastsByProgram(program, section, refresh)
                        .subscribeOn(mIoScheduler)
                        .toList()
                        .observeOn(mMainThreadScheduler)
                        .subscribe(new PodcastSubscriptionObserver());
    }

    public void refreshDownloadedPodcasts() {
        mProgramDataInteractor.refreshDownloadedPodcasts();
    }

    public void download(Podcast podcast) {
        Uri uri = Uri.parse(podcast.getPath());
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(podcast.getTitle())
                .setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,
                        podcast.getAudioId() + ProgramDataInteractor.EXTENSION)
                .setVisibleInDownloadsUi(true);

        mDownloadManager.enqueue(request);
        mProgramDataInteractor.addDownloadingPodcast(podcast);

        refreshDownloadedPodcasts();
    }

    @Override
    public void resume() {
        mDownloadedPodcastSubscription = mProgramDataInteractor.getDownloadedPodcasts()
                .subscribeOn(mIoScheduler)
                .observeOn(mMainThreadScheduler)
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
                        if (mListener != null) {
                            mListener.updateRecyclerViewWithDownloaded(podcasts);
                        }
                    }
                });
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        if (mLoadedPodcastSubscription != null && !mLoadedPodcastSubscription.isUnsubscribed()) {
            mLoadedPodcastSubscription.unsubscribe();
        }
        if (mDownloadedPodcastSubscription != null &&
                !mDownloadedPodcastSubscription.isUnsubscribed()) {
            mDownloadedPodcastSubscription.unsubscribe();
        }
        mListener = null;
        mContext = null;
    }

    public void deletePodcast(Podcast podcast) {
        mProgramDataInteractor.deleteDownload(podcast);
    }

    private class PodcastSubscriptionObserver extends Subscriber<List<Podcast>> {

        @Override
        public void onCompleted() {
            // Ignore
        }

        @Override
        public void onError(Throwable e) {
            ErrorUtil.logException(e);
            mListener.updateRecyclerView(new ArrayList<Podcast>());
        }

        @Override
        public void onNext(List<Podcast> podcasts) {
            mListener.updateRecyclerView(podcasts);
        }
    }
}
