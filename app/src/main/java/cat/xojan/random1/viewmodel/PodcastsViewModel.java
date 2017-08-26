package cat.xojan.random1.viewmodel;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class PodcastsViewModel {

    private final ProgramDataInteractor mProgramDataInteractor;

    @Inject
    public PodcastsViewModel(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    public Single<List<Podcast>> loadDownloadedPodcasts() {
        return mProgramDataInteractor.getDownloadedPodcasts();
    }

    public PublishSubject<List<Podcast>> getDownloadedPodcastsUpdates() {
        return mProgramDataInteractor.getDownloadedPodcastsUpdates();
    }

    public Single<List<Podcast>> loadPodcasts(Program program, Section section,
                                                  boolean refresh) {
        Single<List<Podcast>> loadedPodcasts =
                mProgramDataInteractor.loadPodcasts(program, section, refresh)
                .flatMapIterable(list -> list)
                .map(podcast -> {
                    podcast.setProgramId(program.getId());
                    podcast.setImageUrl(program.getImageUrl());
                    return podcast;
                })
                .toList();

        Single<List<Podcast>> downloadedPodcasts =
                mProgramDataInteractor.getDownloadedPodcasts();

        return Single.zip(loadedPodcasts, downloadedPodcasts,
                (loaded, downloaded) -> {
                    for (Podcast podcast : loaded) {
                        podcast.setFilePath(null);
                        podcast.setState(Podcast.State.LOADED);
                    }

                    for (Podcast download : downloaded) {
                        int index = loaded.indexOf(download);
                        if (index >= 0) {
                            Podcast podcast = loaded.get(index);
                            podcast.setFilePath(download.getFilePath());
                            podcast.setState(download.getState());
                        }
                    }
                    return loaded;
                });
    }

    public void selectedSection(boolean b) {
        mProgramDataInteractor.setSectionSelected(b);
    }

    public Observable<Boolean> exportPodcasts() {
        return mProgramDataInteractor.exportPodcasts();
    }
}
