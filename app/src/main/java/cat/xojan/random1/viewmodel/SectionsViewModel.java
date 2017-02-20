package cat.xojan.random1.viewmodel;

import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import rx.Observable;

public class SectionsViewModel {

    private final ProgramDataInteractor mProgramDataInteractor;

    public SectionsViewModel(ProgramDataInteractor programDataInteractor) {
        mProgramDataInteractor = programDataInteractor;
    }

    public Observable<List<Section>> loadSections(Program program) {
        return mProgramDataInteractor.loadSections(program)
                .flatMap(Observable::from)
                .filter(Section::isActive)
                .filter(section -> section.getType() == Section.Type.SECTION)
                .map(section -> {
                    section.setImageUrl(program.getImageUrl());
                    return section;
                })
                .toList();
    }

    public void selectedSection(boolean selected) {
        mProgramDataInteractor.setSectionSelected(selected);
    }
}
