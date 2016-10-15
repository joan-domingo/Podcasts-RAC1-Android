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
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.domain.model.Section;
import cat.xojan.random1.ui.BasePresenter;
import cat.xojan.random1.ui.fragment.PodcastListFragment;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PodcastListPresenter implements BasePresenter {

    private final DownloadManager mDownloadManager;

    public interface PodcastsListener {
        /** update podcast list*/
        void updateRecyclerView(List<Podcast> podcasts);
        /** update podcasts with downloaded*/
        void updateRecyclerViewWithDownloaded(List<Podcast> podcasts);
        /** refresh recycler view*/
        void updateRecyclerView();
    }

    private final PodcastDataInteractor mPodcastDataInteractor;
    private final Context mContext;
    private Subscription mLoadedPodcastSubscription;
    private PodcastsListener mListener;
    private Subscription mDownloadedPodcastSubscription;

    @Inject
    public PodcastListPresenter(PodcastDataInteractor podcastDataInteractor,  Context context,
                                DownloadManager downloadManager) {
        mPodcastDataInteractor = podcastDataInteractor;
        mContext = context;
        mDownloadManager = downloadManager;
    }

    public void setPodcastsListener(PodcastsListener listener) {
        mListener = listener;
    }

    public void loadPodcasts(Bundle args, List<Podcast> loadedPodcasts) {
        if (loadedPodcasts == null) {
            if (args == null) {
                mLoadedPodcastSubscription = mPodcastDataInteractor.loadPodcasts()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PodcastSubscriptionObserver());
            } else if (args.get(PodcastListFragment.ARG_PROGRAM) != null) {
                Program program = (Program) args.get(PodcastListFragment.ARG_PROGRAM);
                mLoadedPodcastSubscription = mPodcastDataInteractor.loadPodcasts(program)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PodcastSubscriptionObserver());
            } else {
                Section section = (Section) args.get(PodcastListFragment.ARG_SECTION);
                mLoadedPodcastSubscription = mPodcastDataInteractor.loadPodcasts(section)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PodcastSubscriptionObserver());
            }
        } else {
            mListener.updateRecyclerView(loadedPodcasts);
        }
    }

    public void refreshDownloadedPodcasts() {
        mPodcastDataInteractor.refreshDownloadedPodcasts();
    }

    public void download(Podcast podcast) {
        Uri uri = Uri.parse(podcast.getFileUrl());
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(podcast.getProgram())
                .setDescription(podcast.getDescription())
                .setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,
                        podcast.getProgram() + PodcastDataInteractor.SEPARATOR
                                + podcast.getDescription() + PodcastDataInteractor.EXTENSION
                                + PodcastDataInteractor.IMAGE + podcast.getProgramTitle())
                .setVisibleInDownloadsUi(true);

        mDownloadManager.enqueue(request);
        podcast.setState(Podcast.State.DOWNLOADING);
        mListener.updateRecyclerView();
    }

    @Override
    public void resume() {
        mDownloadedPodcastSubscription = mPodcastDataInteractor.getDownloadedPodcasts()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
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
    }

    public void deletePodcast(Podcast podcast) {
        mPodcastDataInteractor.deleteDownload(podcast);
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
