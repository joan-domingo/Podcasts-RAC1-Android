package cat.xojan.random1.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.HourByHourListFragment;
import cat.xojan.random1.ui.fragment.SectionFragment;

public class ProgramViewModel extends BaseObservable {

    private final BaseActivity mActivity;
    private final Program mProgram;
    private final ProgramDataInteractor mProgramDataInteractor;

    public ProgramViewModel(BaseActivity activity, Program program,
                            ProgramDataInteractor programDataInteractor) {
        mActivity = activity;
        mProgram = program;
        mProgramDataInteractor = programDataInteractor;
    }

    public View.OnClickListener onClickProgram() {
        return v -> {
            if (mProgramDataInteractor.isSectionSelected() && mProgram.getSections().size() > 1) {
                SectionFragment sectionListFragment = SectionFragment.newInstance(mProgram);
                mActivity.addFragment(sectionListFragment, SectionFragment.TAG, true);
            } else {
                HourByHourListFragment hourByHourListFragment =
                        HourByHourListFragment.newInstance(mProgram);
                mActivity.addFragment(hourByHourListFragment, HourByHourListFragment.TAG, true);
            }
        };
    }

    public String getImageUrl() {
        return mProgram.getImageUrl();
    }

    public String getTitle() {
        return mProgram.getTitle();
    }
}
