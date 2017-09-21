package cat.xojan.random1.viewmodel;


import android.view.View;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.entities.SectionType;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.fragment.HourByHourListFragment;
import cat.xojan.random1.ui.fragment.SectionFragment;

import static cat.xojan.random1.testutil.DataKt.getProgram1;
import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProgramViewModelTest {

    private ProgramViewModel mViewModel;
    private Program mProgram;
    private BaseActivity mActivity;
    private ProgramDataInteractor mProgramDataInteractor;

    @Before
    public void setUp() {
        mActivity = mock(BaseActivity.class);
        mProgramDataInteractor = mock(ProgramDataInteractor.class);
        mProgram = getProgram1();
        mViewModel = new ProgramViewModel(mActivity, mProgram, mProgramDataInteractor);
    }

    @Test
    public void read_title() {
        assertEquals(mViewModel.getTitle(), mProgram.getTitle());
    }

    @Test
    public void read_image_url() {
        assertEquals(mViewModel.getImageUrl(), mProgram.getImageUrl());
    }

    @Test
    public void click_program_and_show_hour_by_hour() {
        mViewModel.onClickProgram().onClick(new View(mActivity));
        verify(mActivity).addFragment(any(HourByHourListFragment.class),
                eq(HourByHourListFragment.TAG), eq(true));
    }

    @Test
    public void click_program_and_show_sections() {
        mProgram.setSections(getSections());
        when(mProgramDataInteractor.isSectionSelected()).thenReturn(true);
        mViewModel.onClickProgram().onClick(new View(mActivity));
        verify(mActivity).addFragment(any(SectionFragment.class), eq(SectionFragment.TAG), eq(true));
    }

    private List<Section> getSections() {
        List<Section> sections = new ArrayList<>();
        sections.add(new Section("id1", true, SectionType.SECTION));
        sections.add(new Section("id2", true, SectionType.SECTION));
        return sections;
    }
}
