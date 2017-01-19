package cat.xojan.random1.presenter;

import android.content.Context;

import org.junit.Before;

import cat.xojan.random1.domain.interactor.ProgramDataInteractor;

import static org.mockito.Mockito.mock;

public class HomePresenterTest {

    private HomePresenter mPresenter;
    private Context mMockContext;
    private ProgramDataInteractor mMockProgramDataInteractor;

    @Before
    public void setUp() {
        mMockContext = mock(Context.class);
        mMockProgramDataInteractor = mock(ProgramDataInteractor.class);
        mPresenter = new HomePresenter(mMockContext, mMockProgramDataInteractor);
    }
}
