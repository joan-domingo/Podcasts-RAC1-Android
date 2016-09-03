package cat.xojan.random1.ui.presenter;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LatestPodcastPresenter implements BasePresenter {

    private final PodcastDataInteractor mPodcastDataInteractor;
    private Subscription mPodcastSubscription;
    private PodcastsListener mListener;

    public void setPodcastsListener(PodcastsListener listener) {
        mListener = listener;
    }

    public interface PodcastsListener {
        void onPodcastsLoaded(List<Podcast> podcasts);
    }

    @Inject
    public LatestPodcastPresenter(PodcastDataInteractor podcastDataInteractor) {
        mPodcastDataInteractor = podcastDataInteractor;
    }

    public void showPodcasts(String program, List<Podcast> podcasts) {
        if (podcasts == null) {
            if (program == null) {
                mPodcastSubscription = mPodcastDataInteractor.loadPodcasts()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PodcastSubscriptionObserver());
            } else {
                mPodcastSubscription = mPodcastDataInteractor.loadPodcastsByProgram(program)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new PodcastSubscriptionObserver());
            }
        } else {
            mListener.onPodcastsLoaded(podcasts);
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        if (mPodcastSubscription != null && !mPodcastSubscription.isUnsubscribed()) {
            mPodcastSubscription.unsubscribe();
        }
        mListener = null;
    }

    private class PodcastSubscriptionObserver extends Subscriber<List<Podcast>> {
        @Override
        public void onCompleted() {
            //ignore
        }

        @Override
        public void onError(Throwable e) {
            if (!BuildConfig.DEBUG) {
                Crashlytics.logException(e);
            }
            mListener.onPodcastsLoaded(new ArrayList<Podcast>());
        }

        @Override
        public void onNext(List<Podcast> podcasts) {
            mListener.onPodcastsLoaded(podcasts);
        }
    }
}
