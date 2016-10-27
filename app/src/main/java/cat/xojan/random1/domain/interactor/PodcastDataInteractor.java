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
import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.domain.model.Section;
import cat.xojan.random1.domain.repository.PodcastRepository;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class PodcastDataInteractor {

    private static final int NUM_PODCASTS = 30;
    private static final String TAG = PodcastDataInteractor.class.getSimpleName();
    public static final String SEPARATOR = "_";
    public static final String EXTENSION = ".mp3";
    public static final String IMAGE = "programtitle";

    private final PodcastRepository mPodcastRepo;
    private final Context mContext;
    private PublishSubject<List<Podcast>> mDownloadedPodcastsSubject = PublishSubject.create();
    private List<Podcast> mLatestPodcasts;
    private List<Podcast> mPodcastsByProgram;
    private List<Podcast> mPodcastsBySection;
    private Program mProgram;
    private Section mSection;

    @Inject
    public PodcastDataInteractor(PodcastRepository podcastRepository, Context context) {
        mPodcastRepo = podcastRepository;
        mContext = context;
    }

    public Observable<List<Podcast>> loadLatestPodcasts(final boolean refresh) {
        return Observable.create(new Observable.OnSubscribe<List<Podcast>>() {
            @Override
            public void call(Subscriber<? super List<Podcast>> subscriber) {
                try {
                    if (mLatestPodcasts == null || refresh) {
                        mLatestPodcasts = mPodcastRepo.getLatestPodcasts(NUM_PODCASTS);
                    }
                    subscriber.onNext(mLatestPodcasts);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Podcast>> loadPodcastsByProgram(final Program program,
                                                           final boolean refresh) {
        return Observable.create(new Observable.OnSubscribe<List<Podcast>>() {
            @Override
            public void call(Subscriber<? super List<Podcast>> subscriber) {
                try {
                    if (mPodcastsByProgram == null || refresh || !mProgram.equals(program)) {
                        mProgram = program;
                        mPodcastsByProgram = mPodcastRepo.getLatestPodcasts(NUM_PODCASTS,
                                program.getParam());
                    }
                    subscriber.onNext(mPodcastsByProgram);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Podcast>> loadPodcastsBySection(final Section section,
                                                        final boolean refresh) {
        return Observable.create(new Observable.OnSubscribe<List<Podcast>>() {
            @Override
            public void call(Subscriber<? super List<Podcast>> subscriber) {
                try {
                    if (mPodcastsBySection == null || refresh || !mSection.equals(section)) {
                        mSection = section;
                        mPodcastsBySection = mPodcastRepo.getLatestSections(NUM_PODCASTS,
                                section.getParam());
                    }
                    subscriber.onNext(mPodcastsBySection);
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

    public void addDownload(String category, String description, String programTitle) {
        File from = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + category + SEPARATOR + description + EXTENSION
                + IMAGE + programTitle);
        File to = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) +
                File.separator + category + SEPARATOR + description + EXTENSION
                + IMAGE + programTitle);

        if (from.renameTo(to)) {
            Log.d(TAG, "moving download from " + from.getPath() + " to " + to.getPath());
        } else {
            from.delete();
        }
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
        File iternalFileDirectory = mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        for (File podcastFile : iternalFileDirectory.listFiles()) {
            addPodcastToList(podcastList, podcastFile, Environment.DIRECTORY_PODCASTS,
                    Podcast.State.DOWNLOADED);
        }
    }

    private void getDownloading(List<Podcast> podcastList) {
        File iternalFileDirectory = mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        for (File podcastFile : iternalFileDirectory.listFiles()) {
            addPodcastToList(podcastList, podcastFile, Environment.DIRECTORY_DOWNLOADS,
                    Podcast.State.DOWNLOADING);
        }
    }

    private void addPodcastToList(List<Podcast> podcastList, File podcastFile, String directory,
                                  Podcast.State podcastState) {
        String[] fileName = podcastFile.getPath()
                .split(directory + "/")[1].split(SEPARATOR);
        String category = fileName[0];
        String description = fileName[1].split(IMAGE)[0].replace(EXTENSION, "");

        String programTitle = null;
        if (fileName[1].split(IMAGE).length > 1) {
            programTitle = fileName[1].split(IMAGE)[1];
        }

        Podcast podcast = new Podcast(category, description, podcastFile.getPath(),
                podcastState, programTitle);
        podcastList.add(podcast);
    }
}
