package cat.xojan.random1.viewmodel;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;

public class ProgramsViewModel {

    private final ProgramDataInteractor mProgramDataInteractor;

    @Inject
    public ProgramsViewModel(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    public Observable<List<Program>> loadPrograms() {
        return mProgramDataInteractor.loadPrograms()
                .flatMap(Observable::from)
                .filter(Program::isActive)
                .toList();
    }
}
