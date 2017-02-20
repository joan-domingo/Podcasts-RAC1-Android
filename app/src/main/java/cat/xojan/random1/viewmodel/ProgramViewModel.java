package cat.xojan.random1.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.HourByHourListFragment;
import cat.xojan.random1.ui.fragment.SectionFragment;

public class ProgramViewModel extends BaseObservable {

    private final Context mContext;
    private final Program mProgram;
    private final ProgramDataInteractor mProgramDataInteractor;

    public ProgramViewModel(Context context, Program program,
                            ProgramDataInteractor programDataInteractor) {
        mContext = context;
        mProgram = program;
        mProgramDataInteractor = programDataInteractor;
    }

    //TODO test
    public View.OnClickListener onClickProgram() {
        return v -> {
            if (mProgramDataInteractor.isSectionSelected() && mProgram.getSections().size() > 1) {
                SectionFragment sectionListFragment = SectionFragment.newInstance(mProgram);
                ((BaseActivity) mContext).addFragment(sectionListFragment,
                        SectionFragment.TAG, true);
            } else {
                HourByHourListFragment hourByHourListFragment =
                        HourByHourListFragment.newInstance(mProgram);
                ((BaseActivity) mContext).addFragment(hourByHourListFragment,
                        HourByHourListFragment.TAG, true);
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
