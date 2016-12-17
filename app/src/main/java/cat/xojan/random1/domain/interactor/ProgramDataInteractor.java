package cat.xojan.random1.domain.interactor;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cat.xojan.random1.data.PreferencesDownloadPodcastRepository;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.repository.ProgramRepository;
import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;

public class ProgramDataInteractor {

    private static final String PREF_NAME = "shared_preferences";
    private static final String PREF_SECTION = "pref_section";
    public static final String EXTENSION = ".mp3";
    private static final String TAG = ProgramDataInteractor.class.getSimpleName();

    private final ProgramRepository mProgramRepo;
    private final Context mContext;
    private final PreferencesDownloadPodcastRepository mDownloadRepo;
    private List<Program> mPrograms;
    private List<Podcast> mPodcastsByProgram;
    private Program mProgram;
    private List<Podcast> mPodcastsBySection;
    private Section mSection;
    private PublishSubject<List<Podcast>> mDownloadedPodcastsSubject = PublishSubject.create();

    @Inject
    public ProgramDataInteractor(ProgramRepository programRepository,
                                 PreferencesDownloadPodcastRepository downloadRepository,
                                 Context context) {
        mProgramRepo = programRepository;
        mDownloadRepo = downloadRepository;
        mContext = context;
    }

    public Observable<Program> loadPrograms() {
        return Observable.create(new Observable.OnSubscribe<Program>() {
            @Override
            public void call(Subscriber<? super Program> subscriber) {
                try {
                    if (mPrograms == null) {
                        mPrograms = mProgramRepo.getProgramList();
                    }
                    for (Program program : mPrograms) {
                        subscriber.onNext(program);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<Section> loadSections(final Program program) {
        return Observable.create(new Observable.OnSubscribe<Section>() {
            @Override
            public void call(Subscriber<? super Section> subscriber) {
                try {
                    List<Section> sections = new ArrayList<>(program.getSections());
                    sections.remove(0);
                    for (Section section : sections) {
                        section.setImageUrl(program.getImageUrl());
                        subscriber.onNext(section);
                    }
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public boolean getSectionSelected() {
        return mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(PREF_SECTION, false);
    }

    public void getSectionSelected(boolean selected) {
        mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit()
                .putBoolean(PREF_SECTION, selected).apply();
    }

    public Observable<Podcast> loadPodcastsByProgram(final Program program, final Section section,
                                            final boolean refresh) {
        if (section != null) {
            return Observable.create(new Observable.OnSubscribe<Podcast>() {
                @Override
                public void call(Subscriber<? super Podcast> subscriber) {
                    try {
                        if (mPodcastsBySection == null || refresh || !mSection.equals(section)) {
                            mSection = section;
                            mPodcastsBySection = mProgramRepo.getPodcastBySection(program.getId(),
                                    section.getId());
                        }
                        for (Podcast podcast : mPodcastsBySection) {
                            podcast.setImageUrl(program.getImageUrl());
                            subscriber.onNext(podcast);
                        }
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            });
        } else {
            return Observable.create(new Observable.OnSubscribe<Podcast>() {
                @Override
                public void call(Subscriber<? super Podcast> subscriber) {
                    try {
                        if (mPodcastsByProgram == null || refresh || !mProgram.equals(program)) {
                            mProgram = program;
                            mPodcastsByProgram = mProgramRepo.getPodcastByProgram(program.getId());
                        }
                        for (Podcast podcast : mPodcastsByProgram) {
                            podcast.setImageUrl(program.getImageUrl());
                            subscriber.onNext(podcast);
                        }
                        subscriber.onCompleted();
                    } catch (Exception e) {
                        subscriber.onError(e);
                    }
                }
            });
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
        refreshDownloadedPodcasts();
    }

    public Observable<List<Podcast>> getDownloadedPodcasts() {
        return mDownloadedPodcastsSubject;
    }

    public void refreshDownloadedPodcasts() {
        try {
            Set<Podcast> podcastList = new HashSet<>();
            Set<Podcast> downloading = mDownloadRepo.getDownloadingPodcasts();
            Set<Podcast> downloaded = mDownloadRepo.getDownloadedPodcasts();
            podcastList.addAll(downloading);
            podcastList.addAll(downloaded);

            Log.d(TAG, "Downloading: " + downloading.size() + ", downloaded: " + downloaded.size());
            mDownloadedPodcastsSubject.onNext(new ArrayList<>(podcastList));
        } catch (Exception e) {
            mDownloadedPodcastsSubject.onError(e);
        }
    }

    public void deleteDownload(Podcast podcast) {
        File file = new File(podcast.getFilePath());
        if (file.delete()) {
            mDownloadRepo.deleteDownloadedPodcast(podcast);
            refreshDownloadedPodcasts();
        }
    }

    public boolean addDownloadingPodcast(Podcast podcast) {
        return mDownloadRepo.addDownloadingPodcast(podcast);
    }

    @VisibleForTesting
    public void setProgramsData(List<Program> programs) {
        mPrograms = programs;
    }

    @VisibleForTesting
    public List<Program> getProgramData() {
        return mPrograms;
    }

    @Nullable
    public String getDownloadedPodcastTitle(String audioId) {
        return mDownloadRepo.getDownloadedPodcastTitle(audioId);
    }
}
