package cat.xojan.random1.viewmodel;

import android.view.View;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.PodcastListFragment;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SectionViewModelTest {

    private BaseActivity mActivity;
    private Program mProgram;
    private SectionViewModel mViewModel;
    private Section mSection;

    @Before
    public void setUp() {
        mActivity = mock(BaseActivity.class);
        mProgram = new Program("id", true);
        mSection = new Section("id", true, Section.Type.SECTION);
        mSection.setTitle("title");
        mSection.setImageUrl("www.image.url");
        mViewModel = new SectionViewModel(mActivity, mSection, mProgram);
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
