package cat.xojan.random1.domain.interactor;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cat.xojan.random1.commons.Log;
import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.repository.ProgramRepository;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ProgramDataInteractor {

    private static final String PREF_NAME = "shared_preferences";
    private static final String PREF_SECTION = "pref_section";
    public static final String EXTENSION = ".mp3";
    private static final String TAG = ProgramDataInteractor.class.getSimpleName();

    private final ProgramRepository mProgramRepo;
    private final Context mContext;
    private final PreferencesDownloadPodcastRepository mDownloadRepo;
    private Observable<List<Program>> mPrograms;
    private Observable<List<Podcast>> mPodcastsByProgram;
    private Program mProgram;
    private Observable<List<Podcast>> mPodcastsBySection;
    private Section mSection;
    private PublishSubject<List<Podcast>> mDownloadedPodcastsSubject;

    @Inject
    public ProgramDataInteractor(ProgramRepository programRepository,
                                 PreferencesDownloadPodcastRepository downloadRepository,
                                 Context context) {
        mProgramRepo = programRepository;
        mDownloadRepo = downloadRepository;
        mContext = context;
        mDownloadedPodcastsSubject = PublishSubject.create();
    }

    public Observable<List<Program>> loadPrograms() {
        try {
            if (mPrograms == null) {
                mPrograms = mProgramRepo.getProgramListObservable();
            }
            return mPrograms;
        } catch (IOException e) {
            return Observable.error(e);
        }
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

    public Observable<List<Podcast>> loadPodcasts(final Program program, final Section section,
                                                  final boolean refresh) {
        try {
            if (section != null) {
                if (mPodcastsBySection == null || refresh || !mSection.equals(section)) {
                    mSection = section;
                    mPodcastsBySection = mProgramRepo.getPodcastBySection(program.getId(),
                            section.getId());
                }
                return mPodcastsBySection;
            } else {
                if (mPodcastsByProgram == null || refresh || !mProgram.equals(program)) {
                    mProgram = program;
                    mPodcastsByProgram = mProgramRepo.getPodcastByProgram(program.getId());
                }
                return mPodcastsByProgram;
            }
        } catch (IOException e) {
            return Observable.error(e);
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

    public Observable<List<Podcast>> getDownloadedPodcasts() {
        return Observable.just(fetchDownloadedPodcasts());
    }

    public Observable<List<Podcast>> getDownloadedPodcastsUpdates() {
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

    public boolean addDownloadingPodcast(Podcast podcast) {
        return mDownloadRepo.addDownloadingPodcast(podcast);
    }

    @VisibleForTesting
    public void setProgramsData(Observable<List<Program>> programs) {
        mPrograms = programs;
    }

    @Nullable
    public String getDownloadedPodcastTitle(String audioId) {
        return mDownloadRepo.getDownloadedPodcastTitle(audioId);
    }
}
