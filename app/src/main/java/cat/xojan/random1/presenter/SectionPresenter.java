package cat.xojan.random1.presenter;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.BasePresenter;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SectionPresenter implements BasePresenter {

    private final ProgramDataInteractor mProgramInteractor;
    private SectionListUi mListener;

    public interface SectionListUi {
        void updateRecyclerView(List<Section> sections);
    }

    @Inject
    public SectionPresenter(ProgramDataInteractor programDataInteractor) {
        mProgramInteractor = programDataInteractor;
    }

    public void setListener(SectionListUi listener) {
        mListener = listener;
    }

    public void loadSections(Program program) {
        mProgramInteractor.loadSections(program)
                .subscribeOn(Schedulers.newThread())
                .filter(new Func1<Section, Boolean>() {
                    @Override
                    public Boolean call(Section section) {
                        return section.isActive();
                    }
                })
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Section>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ErrorUtil.logException(e);
                    }

                    @Override
                    public void onNext(List<Section> sections) {
                        mListener.updateRecyclerView(sections);
                    }
                });
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

    public void selectedSection(boolean selected) {
        mProgramInteractor.setSectionSelected(selected);
    }
}
