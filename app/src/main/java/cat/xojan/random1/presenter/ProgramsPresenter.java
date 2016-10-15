package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProgramsPresenter implements BasePresenter {

    private final ProgramDataInteractor mProgramDataInteractor;
    private ProgramListener mListener;

    public ProgramsPresenter(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        mListener = null;
    }

    public void setPodcastsListener(ProgramListener listener) {
        mListener = listener;
    }

    public void showPrograms() {
        mProgramDataInteractor.loadPrograms()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<List<Program>>() {
                    @Override
                    public void onCompleted() {
                        // Ignore
                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorUtil.logException(e);
                        mListener.onProgramsLoaded(new ArrayList<Program>());
                    }

                    @Override
                    public void onNext(List<Program> itemPrograms) {
                        mListener.onProgramsLoaded(itemPrograms);
                    }
                });
    }

    public boolean showSections() {
        return mProgramDataInteractor.getSectionSelected();
    }

    public interface ProgramListener {
        void onProgramsLoaded(List<Program> programs);
    }
}
