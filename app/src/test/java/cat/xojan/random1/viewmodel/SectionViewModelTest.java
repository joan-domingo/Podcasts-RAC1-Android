package cat.xojan.random1.viewmodel;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;

import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SectionViewModelTest {

    private Context mContext;
    private Program mProgram;
    private SectionViewModel mViewModel;
    private Section mSection;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
        mProgram = new Program("id", true);
        mSection = new Section("id", true, Section.Type.SECTION);
        mSection.setTitle("title");
        mSection.setImageUrl("www.image.url");
        mViewModel = new SectionViewModel(mContext, mSection, mProgram);
    }

    @Test
    public void read_title() {
        assertEquals(mViewModel.getTitle(), mSection.getTitle());
    }

    @Test
    public void read_image_url() {
        assertEquals(mViewModel.getImageUrl(), mSection.getImageUrl());
    }
}
