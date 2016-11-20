package cat.xojan.random1.domain.interactor;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.repository.ProgramRepository;
import rx.Observable;
import rx.Subscriber;

public class ProgramDataInteractor {

    private static final String PREF_NAME = "shared_preferences";
    private static final String PREF_SECTION = "pref_section";

    private final ProgramRepository mProgramRepo;
    private final Context mContext;
    private List<Program> mPrograms;

    @Inject
    public ProgramDataInteractor(ProgramRepository programRepository, Context context) {
        mProgramRepo = programRepository;
        mContext = context;
    }

    public Observable<List<Program>> loadPrograms() {
        return Observable.create(new Observable.OnSubscribe<List<Program>>() {
            @Override
            public void call(Subscriber<? super List<Program>> subscriber) {
                try {
                    if (mPrograms == null) {
                        mPrograms = mProgramRepo.getProgramList();
                    }
                    subscriber.onNext(mPrograms);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public Observable<List<Section>> loadSections(final Program program) {
        return Observable.create(new Observable.OnSubscribe<List<Section>>() {
            @Override
            public void call(Subscriber<? super List<Section>> subscriber) {
                try {
                    List<Section> sections = new ArrayList<Section>(program.getSections());
                    sections.remove(0);
                    subscriber.onNext(sections);
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
}
