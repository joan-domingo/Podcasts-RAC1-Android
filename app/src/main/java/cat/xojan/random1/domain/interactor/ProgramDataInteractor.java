package cat.xojan.random1.domain.interactor;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Log;

import org.intellij.lang.annotations.Flow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.EventLogger;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.repository.ProgramRepository;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class ProgramDataInteractor {

    private static final String PREF_NAME = "shared_preferences";
    private static final String PREF_SECTION = "pref_section";
    public static final String EXTENSION = ".mp3";
    private static final String TAG = ProgramDataInteractor.class.getSimpleName();

    private final ProgramRepository mProgramRepo;
    private final Context mContext;
    private final PreferencesDownloadPodcastRepository mDownloadRepo;
    private final DownloadManager mDownloadManager;
    private List<Program> mPrograms;
    private Flowable<List<Podcast>> mPodcastsByProgram;
    private Program mProgram;
    private Flowable<List<Podcast>> mPodcastsBySection;
    private Section mSection;
    private PublishSubject<List<Podcast>> mDownloadedPodcastsSubject;
    private final EventLogger mEventLogger;

    @Inject
    public ProgramDataInteractor(ProgramRepository programRepository,
                                 PreferencesDownloadPodcastRepository downloadRepository,
                                 Context context,
                                 DownloadManager downloadManager,
                                 EventLogger eventLogger) {
        mProgramRepo = programRepository;
        mDownloadRepo = downloadRepository;
        mContext = context;
        mDownloadedPodcastsSubject = PublishSubject.create();
        mDownloadManager = downloadManager;
        mEventLogger = eventLogger;
    }

    public Observable<List<Program>> loadPrograms() {
        return Observable.create(subscriber -> {
            try {
                if (mPrograms == null) {
                    mPrograms = mProgramRepo.getPrograms();
                }
                subscriber.onNext(mPrograms);
                subscriber.onComplete();
            } catch (IOException e) {
                subscriber.onError(e);
            }
        });
    }

    public Observable<List<Section>> loadSections(Program program) {
        return Observable.just(program.getSections());
    }

    public boolean isSectionSelected() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(PREF_SECTION, false);
    }

    public void setSectionSelected(boolean selected) {
        mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putBoolean(PREF_SECTION, selected).apply();
    }

    public Flowable<List<Podcast>> loadPodcasts(final Program program, final Section section,
                                                final boolean refresh) {
        try {
            if (section != null) {
                if (mPodcastsBySection == null || refresh || !mSection.equals(section)) {
                    mSection = section;
                    mPodcastsBySection = mProgramRepo.getPodcast(program.getId(),
                            section.getId());
                }
                return mPodcastsBySection;
            } else {
                if (mPodcastsByProgram == null || refresh || !mProgram.equals(program)) {
                    mProgram = program;
                    mPodcastsByProgram = mProgramRepo.getPodcast(program.getId(), null);
                }
                return mPodcastsByProgram;
            }
        } catch (IOException e) {
            return Flowable.error(e);
        }
    }

    public void addDownload(String audioId) {
        File from = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) +
                File.separator + audioId + EXTENSION);
        File to = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS) +
                File.separator + audioId + EXTENSION);

        if (from.renameTo(to)) {
            Log.d(TAG, "moving download from " + from.getPath() + " to " + to.getPath());
            mDownloadRepo.setPodcastAsDownloaded(audioId, to.getPath());
        } else {
            from.delete();
            to.delete();
        }
    }

    public Single<List<Podcast>> getDownloadedPodcasts() {
        return Single.just(fetchDownloadedPodcasts());
    }

    public PublishSubject<List<Podcast>> getDownloadedPodcastsUpdates() {
        return mDownloadedPodcastsSubject;
    }

    public void refreshDownloadedPodcasts() {
        mDownloadedPodcastsSubject.onNext(fetchDownloadedPodcasts());
    }

    private List<Podcast> fetchDownloadedPodcasts() {
        Set<Podcast> podcastList = new HashSet<>();
        Set<Podcast> downloading = mDownloadRepo.getDownloadingPodcasts();
        Set<Podcast> downloaded = mDownloadRepo.getDownloadedPodcasts();
        podcastList.addAll(downloading);
        podcastList.addAll(downloaded);

        Log.d(TAG, "Downloading: " + downloading.size() + ", downloaded: " + downloaded.size());
        return new ArrayList<>(podcastList);
    }

    public void deleteDownload(Podcast podcast) {
        File file = new File(podcast.getFilePath());
        if (file.delete()) {
            mDownloadRepo.deleteDownloadedPodcast(podcast);
        }
    }

    public void download(Podcast podcast) {
        Uri uri = Uri.parse(podcast.getPath());
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(podcast.getTitle())
                .setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,
                        podcast.getAudioId() + ProgramDataInteractor.EXTENSION)
                .setVisibleInDownloadsUi(true);

        long reference = mDownloadManager.enqueue(request);
        podcast.setDownloadReference(reference);
        addDownloadingPodcast(podcast);
    }

    private boolean addDownloadingPodcast(Podcast podcast) {
        return mDownloadRepo.addDownloadingPodcast(podcast);
    }

    @VisibleForTesting
    public void setProgramsData(List<Program> programs) {
        mPrograms = programs;
    }

    @Nullable
    public String getDownloadedPodcastTitle(String audioId) {
        return mDownloadRepo.getDownloadedPodcastTitle(audioId);
    }

    public void deleteDownloading(long reference) {
        Podcast podcast = null;
        for (Podcast pod : mDownloadRepo.getDownloadingPodcasts()) {
            if (reference == pod.getDownloadReference()) {
                podcast = pod;
            }
        }
        mDownloadRepo.deleteDownloadingPodcast(podcast);
    }

    public Observable<Boolean> exportPodcasts() {
        mEventLogger.logExportedPodcastAction();
        return Observable.create(e -> {
            File iternalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
            File externalFilesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PODCASTS);

            externalFilesDir.mkdirs();

            for (File podcastFile : iternalFileDir.listFiles()) {
                String audioId = podcastFile.getPath()
                        .split(Environment.DIRECTORY_PODCASTS + "/")[1].replace(".mp3", "");
                String podcastTitle = getDownloadedPodcastTitle(audioId);

                if (!TextUtils.isEmpty(podcastTitle)) {
                    podcastTitle = podcastTitle.replace("/", "-");
                    File dest = new File(externalFilesDir, podcastTitle + ".mp3");
                    copy(podcastFile, dest);
                    mEventLogger.logExportedPodcast(podcastTitle);
                }
            }
            e.onNext(true);
        });
    }

    private void copy(File src, File dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
