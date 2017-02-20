package cat.xojan.random1.viewmodel;


import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ProgramViewModelTest {

    private ProgramViewModel mViewModel;
    private Program mProgram;
    private Context mContext;
    private ProgramDataInteractor mProgramDataInteractor;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mProgramDataInteractor = mock(ProgramDataInteractor.class);

        mProgram = new Program("program1", true);
        mProgram.setTitle("title program 1");
        mProgram.setImageUrl("http://www.url.com/image");

        mViewModel = new ProgramViewModel(mContext, mProgram, mProgramDataInteractor);
    }

    @Test
    public void read_title() {
        assertEquals(mViewModel.getTitle(), mProgram.getTitle());
    }

    @Test
    public void read_image_url() {
        assertEquals(mViewModel.getImageUrl(), mProgram.getImageUrl());
    }
}
