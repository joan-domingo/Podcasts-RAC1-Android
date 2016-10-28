package cat.xojan.random1.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.domain.model.Program;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.presenter.ProgramsPresenter;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.adapter.ProgramListAdapter;

public class ProgramFragment extends BaseFragment implements ProgramsPresenter.ProgramListener,
        ProgramListAdapter.RecyclerViewListener {

    @Inject ProgramsPresenter mPresenter;

    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mEmptyList;
    private RecyclerView mRecyclerView;

    private ProgramListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mEmptyList = (TextView) view.findViewById(R.id.empty_list);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshListener());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutManager(getResources().getConfiguration().orientation);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(HomeComponent.class).inject(this);
        mPresenter.setPodcastsListener(this);
        loadPrograms();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.destroy();
        }
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void onProgramsLoaded(List<Program> programs) {
        mAdapter = new ProgramListAdapter(programs, this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefresh.setRefreshing(false);

        if (programs.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        } else {
            mEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(Program program) {
        if (mPresenter.showSections() && program.getSections().size() > 1) {
            SectionListFragment sectionListFragment = SectionListFragment.newInstance(program);
            ((BaseActivity) getActivity()).addFragment(R.id.container_fragment, sectionListFragment,
                    SectionListFragment.TAG, true);
        } else {
            HourByHourListFragment hourByHourListFragment =
                    HourByHourListFragment.newInstance(program);
            ((BaseActivity) getActivity()).addFragment(R.id.container_fragment,
                    hourByHourListFragment, HourByHourListFragment.TAG, true);
        }
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }

    private void loadPrograms() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                mPresenter.showPrograms();
            }
        }, 0);
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            loadPrograms();
        }
    }
}
