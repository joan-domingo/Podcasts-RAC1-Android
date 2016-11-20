package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProgramsPresenter implements BasePresenter {

    private final ProgramDataInteractor mProgramDataInteractor;
    private ProgramListener mListener;
    private Subscription mSubscription;

    @Inject
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
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mListener = null;
    }

    public void setPodcastsListener(ProgramListener listener) {
        mListener = listener;
    }

    public void showPrograms() {
        mSubscription = mProgramDataInteractor.loadPrograms()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgramSubscriptionObserver());
    }

    public boolean showSections() {
        return mProgramDataInteractor.getSectionSelected();
    }

    public interface ProgramListener {
        void onProgramsLoaded(List<Program> programs);
    }

    private class ProgramSubscriptionObserver extends Subscriber<List<Program>> {
        @Override
        public void onCompleted() {
            // Ignore
        }

        @Override
        public void onError(Throwable e) {
            ErrorUtil.logException(e);
            if (mListener != null) {
                mListener.onProgramsLoaded(new ArrayList<Program>());
            }
        }

        @Override
        public void onNext(List<Program> programs) {
            if (mListener != null) {
                mListener.onProgramsLoaded(programs);
            }
        }
    }
}
