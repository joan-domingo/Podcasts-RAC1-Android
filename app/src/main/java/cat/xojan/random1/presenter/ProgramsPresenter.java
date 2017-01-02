package cat.xojan.random1.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.BasePresenter;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

public class ProgramsPresenter implements BasePresenter {

    private final ProgramDataInteractor mProgramDataInteractor;
    private final Scheduler mIoScheduler;
    private final Scheduler mMainThreadScheduler;
    private Subscription mSubscription;

    @Inject
    public ProgramsPresenter(ProgramDataInteractor programDataInteractor, Scheduler io,
                             Scheduler main) {
        mProgramDataInteractor = programDataInteractor;
        mIoScheduler = io;
        mMainThreadScheduler = main;
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
    }

    public void showPrograms(ProgramListener listener) {
        mSubscription = mProgramDataInteractor.loadPrograms()
                .subscribeOn(mIoScheduler)
                .filter(new Func1<Program, Boolean>() {
                    @Override
                    public Boolean call(Program program) {
                        return program.isActive();
                    }
                })
                .toList()
                .observeOn(mMainThreadScheduler)
                .subscribe(new ProgramSubscriptionObserver(listener));
    }

    public boolean showSections() {
        return mProgramDataInteractor.getSectionSelected();
    }

    public interface ProgramListener {
        void onProgramsLoaded(List<Program> programs);
    }

    private class ProgramSubscriptionObserver extends Subscriber<List<Program>> {

        private final ProgramListener mListener;

        ProgramSubscriptionObserver(ProgramListener listener) {
            mListener = listener;
        }

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
        public void onNext(List<Program> programs) {
            mListener.onProgramsLoaded(programs);
        }
    }
}
