package cat.xojan.random1.domain.interactor;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.domain.repository.PodcastRepository;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class PodcastDataInteractor {

    private static final int NUM_PODCASTS = 30;
    private static final String TAG = PodcastDataInteractor.class.getSimpleName();
    public static final String SEPARATOR = "_";
    public static final String EXTENSION = ".mp3";

    private final PodcastRepository mPodcastRepo;
    private final Context mContext;
    private PublishSubject<List<Podcast>> mDownloadedPodcastsSubject = PublishSubject.create();

    @Inject
    public PodcastDataInteractor(PodcastRepository podcastRepository, Context context) {
        mPodcastRepo = podcastRepository;
        mContext = context;
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

    public void refreshDownloadedPodcasts() {
        try {
            List<Podcast> podcastList = new ArrayList<>();
            getDownloading(podcastList);
            int downloading = podcastList.size();
            Log.d(TAG, "downloading: " + downloading);
            getDownloaded(podcastList);
            int downloaded = podcastList.size() - downloading;
            Log.d(TAG, "downloaded: " + downloaded);

            mDownloadedPodcastsSubject.onNext(podcastList);
        } catch (Exception e) {
            mDownloadedPodcastsSubject.onError(e);
        };
    }

    public void addDownload(String category, String description) {
        File from = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + category + SEPARATOR + description + EXTENSION);
        File to = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) +
                File.separator + category + SEPARATOR + description + EXTENSION);

        Log.d(TAG, "moving download from " + from.getPath() + " to " + to.getPath());
        from.renameTo(to);
        refreshDownloadedPodcasts();
    }

    public void deleteDownload(Podcast podcast) {
        File file = new File(podcast.getFilePath());
        if (file.delete()) {
            refreshDownloadedPodcasts();
        }
    }

    public Observable<List<Podcast>> getDownloadedPodcasts() {
        return mDownloadedPodcastsSubject;
    }

    private void getDownloaded(List<Podcast> podcastList) {
        File iternalFileDirectory = mContext.getExternalFilesDir(
                Environment.DIRECTORY_PODCASTS);

        for (File podcastFile : iternalFileDirectory.listFiles()) {
            String[] fileName = podcastFile.getPath()
                    .split(Environment.DIRECTORY_PODCASTS + "/")[1].split(SEPARATOR);
            String category = fileName[0];
            String description = fileName[1].replace(EXTENSION, "");

            Podcast podcast = new Podcast(category, description, podcastFile.getPath(),
                    Podcast.State.DOWNLOADED);
            podcastList.add(podcast);
        }
    }

    private void getDownloading(List<Podcast> podcastList) {
        File iternalFileDirectory = mContext.getExternalFilesDir(
                Environment.DIRECTORY_DOWNLOADS);

        for (File podcastFile : iternalFileDirectory.listFiles()) {
            String[] fileName = podcastFile.getPath()
                    .split(Environment.DIRECTORY_DOWNLOADS + "/")[1].split(SEPARATOR);
            String category = fileName[0];
            String description = fileName[1].replace(EXTENSION, "");

            Podcast podcast = new Podcast(category, description, podcastFile.getPath(),
                    Podcast.State.DOWNLOADING);
            podcastList.add(podcast);
        }
    }
}
