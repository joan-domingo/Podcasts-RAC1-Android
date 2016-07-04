package cat.xojan.random1.ui.presenter;

import java.util.List;

import cat.xojan.random1.domain.entity.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.BasePresenter;

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

    public void showPrograms(List<Program> programs) {
        if (programs == null) {
            programs = mProgramDataInteractor.loadPrograms();
        }
        mListener.onProgramsLoaded(programs);
    }

    public interface ProgramListener {
        void onProgramsLoaded(List<Program> programs);
    }
}
