package cat.xojan.random1.viewmodel;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import io.reactivex.Single;

public class ProgramsViewModel {

    private final ProgramDataInteractor mProgramDataInteractor;

    @Inject
    public ProgramsViewModel(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    public Single<List<Program>> loadPrograms() {
        return mProgramDataInteractor.loadPrograms()
                .flatMapIterable(list -> list)
                .filter(Program::getActive)
                .toList();
    }
}
