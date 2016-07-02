package cat.xojan.random1.domain.interactor;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.domain.repository.PodcastRepository;
import rx.Observable;
import rx.Subscriber;

public class PodcastDataInteractor {

    private static final int NUM_PODCASTS = 30;
    private final PodcastRepository mPodcastRepo;

    @Inject
    public PodcastDataInteractor(PodcastRepository podcastRepository) {
        mPodcastRepo = podcastRepository;
    }

    public Observable<List<Podcast>> loadPodcasts() {
        return Observable.create(new Observable.OnSubscribe<List<Podcast>>() {
            @Override
            public void call(Subscriber<? super List<Podcast>> subscriber) {
                try {
                    subscriber.onNext(mPodcastRepo.getLatestPodcasts(NUM_PODCASTS));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Podcast>> loadPodcastsByProgram(final String program) {
        return Observable.create(new Observable.OnSubscribe<List<Podcast>>() {
            @Override
            public void call(Subscriber<? super List<Podcast>> subscriber) {
                try {
                    subscriber.onNext(mPodcastRepo.getLatestPodcastsByProgram(NUM_PODCASTS,
                            program));
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
