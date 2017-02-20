package cat.xojan.random1.viewmodel;

import android.content.Context;
import android.view.View;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.PodcastListFragment;

public class SectionViewModel {

    private final Context mContext;
    private final Section mSection;
    private final Program mProgram;

    public SectionViewModel(Context context, Section section, Program program) {
        mContext = context;
        mSection = section;
        mProgram = program;
    }

    //TODO test
    public View.OnClickListener onClickSection() {
        return view -> {
            PodcastListFragment podcastListFragment =
                    PodcastListFragment.newInstance(mSection, mProgram);
            ((BaseActivity) mContext).addFragment(podcastListFragment,
                    PodcastListFragment.TAG, true);
        };
    }

    public String getImageUrl() {
        return mSection.getImageUrl();
    }

    public String getTitle() {
        return mSection.getTitle();
    }
}
