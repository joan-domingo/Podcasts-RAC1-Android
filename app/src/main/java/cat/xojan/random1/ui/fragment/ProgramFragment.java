package cat.xojan.random1.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.adapter.ProgramListAdapter;
import cat.xojan.random1.presenter.ProgramsPresenter;

public class ProgramFragment extends BaseFragment implements ProgramsPresenter.ProgramListener,
        ProgramListAdapter.RecyclerViewListener {

    @Inject ProgramsPresenter mPresenter;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout mSwipeRefresh;

    private ProgramListAdapter mAdapter;
    private Unbinder unbinder;
    private List<Program> mPrograms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSwipeRefresh.setEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutManager(getResources().getConfiguration().orientation);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setLayoutManager(newConfig.orientation);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(HomeComponent.class).inject(this);
        mPresenter.setPodcastsListener(this);
        mPresenter.showPrograms(mPrograms);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.destroy();
        mRecyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onProgramsLoaded(List<Program> programs) {
        mPrograms = programs;
        mAdapter = new ProgramListAdapter(programs, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(Program program) {
        PodcastListFragment podcastListFragment = PodcastListFragment
                .newInstance(program.getParam());
        ((BaseActivity) getActivity()).addFragment(R.id.container_fragment,
                podcastListFragment, PodcastListFragment.TAG, true);
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }
}
