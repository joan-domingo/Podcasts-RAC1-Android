package cat.xojan.random1.domain.interactor;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.domain.repository.ProgramRepository;

public class ProgramDataInteractor {

    private final ProgramRepository mProgramRepo;

    @Inject
    public ProgramDataInteractor(ProgramRepository programRepository) {
        mProgramRepo = programRepository;
    }

    public List<Program> loadPrograms() {
        return mProgramRepo.getProgramList();
    }
}
