package cat.xojan.random1.viewmodel;

import android.view.View;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.PodcastListFragment;

import static cat.xojan.random1.testutil.DataKt.getProgram1;
import static cat.xojan.random1.testutil.DataKt.getSection1;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SectionViewModelTest {

    private BaseActivity mActivity;
    private SectionViewModel mViewModel;
    private Section mSection;

    @Before
    public void setUp() {
        mActivity = mock(BaseActivity.class);
        mSection = getSection1();
        mViewModel = new SectionViewModel(mActivity, mSection, getProgram1());
    }

    @Test
    public void read_title() {
        assertEquals(mViewModel.getTitle(), mSection.getTitle());
    }

    @Test
    public void read_image_url() {
        assertEquals(mViewModel.getImageUrl(), mSection.getImageUrl());
    }

    @Test
    public void click_section() {
        mViewModel.onClickSection().onClick(new View(mActivity));
        verify(mActivity).addFragment(any(PodcastListFragment.class),
                eq(PodcastListFragment.TAG), eq(true));
    }
}
