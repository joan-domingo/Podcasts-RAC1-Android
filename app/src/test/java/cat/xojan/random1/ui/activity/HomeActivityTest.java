package cat.xojan.random1.ui.activity;

import android.view.Menu;
import android.view.MenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.ui.fragment.DownloadsFragment;
import cat.xojan.random1.ui.fragment.ProgramFragment;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class HomeActivityTest {

    private HomeActivity mActivity;
    ProgramFragment mProgramFragment;
    DownloadsFragment mDownloadsFragment;

    @Before
    public void setUp() {
        mActivity = Robolectric.setupActivity(HomeActivity.class);
        mProgramFragment = (ProgramFragment) mActivity.mFragmentAdapter.getItem(0);
        mDownloadsFragment = (DownloadsFragment) mActivity.mFragmentAdapter.getItem(1);
    }

    @Test
    public void inflate_menu() {
        final Menu menu = shadowOf(mActivity).getOptionsMenu();

        MenuItem export = menu.findItem(R.id.action_export_podcasts);
        String expectedTitle = mActivity.getString(R.string.export_podcasts);

        assertEquals(export.getTitle().toString(), expectedTitle);
    }

    @Test
    public void show_sections() {

        //RecyclerView recyclerView = (RecyclerView) mActivity.findViewById(R.id.recycler_view);
        //ProgramListAdapter adapter = (ProgramListAdapter) recyclerView.getAdapter();

        //recyclerView.getChildAt(0).performClick();
    }

    private List<Podcast> getPodcastList() {
        List<Podcast> podcasts = new ArrayList<>();
        Podcast podcast1 = new Podcast("path1", "program6", "Program title 1");
        Podcast podcast2 = new Podcast("path2", "program6", "Program title 2");

        podcasts.add(podcast1);
        podcasts.add(podcast2);
        return podcasts;
    }
}
