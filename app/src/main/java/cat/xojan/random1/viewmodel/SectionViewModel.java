package cat.xojan.random1.viewmodel;

import android.view.View;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.PodcastListFragment;

public class SectionViewModel {

    private final BaseActivity mActivity;
    private final Section mSection;
    private final Program mProgram;

    public SectionViewModel(BaseActivity activity, Section section, Program program) {
        mActivity = activity;
        mSection = section;
        mProgram = program;
    }

    public View.OnClickListener onClickSection() {
        return view -> {
            PodcastListFragment podcastListFragment =
                    PodcastListFragment.newInstance(mSection, mProgram);
            mActivity.addFragment(podcastListFragment, PodcastListFragment.TAG, true);
        };
    }

    public String getImageUrl() {
        return mSection.getImageUrl();
    }

    public String getTitle() {
        return mSection.getTitle();
    }
}
