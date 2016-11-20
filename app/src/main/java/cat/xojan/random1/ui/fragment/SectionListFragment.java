package cat.xojan.random1.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.presenter.SectionPresenter;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.adapter.SectionListAdapter;

public class SectionListFragment extends BaseFragment implements SectionPresenter.SectionListUi,
        SectionListAdapter.RecyclerViewListener {

    public static final String TAG = SectionListFragment.class.getSimpleName();
    private static final String ARG_PROGRAM = "program_arg";

    @Inject SectionPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private ActionBar mActionBar;

    public static SectionListFragment newInstance(Program program) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROGRAM, program);

        SectionListFragment sectionListFragment = new SectionListFragment();
        sectionListFragment.setArguments(args);

        return sectionListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        mSwipeRefresh.setEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sections, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(HomeComponent.class).inject(this);
        mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        mPresenter.setListener(this);
        Program program = (Program) getArguments().get(ARG_PROGRAM);
        mPresenter.loadSections(program);
        mPresenter.selectedSection(true);
        getActivity().setTitle(program.getCategory());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleOnBackPressed();
                return true;
            case R.id.action_hour_by_hour:
                showHourByHour();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
        showBackArrow(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
        showBackArrow(false);
    }

    @Override
    public void updateRecyclerView(List<Section> sections) {
        SectionListAdapter mAdapter = new SectionListAdapter(sections, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(Section section) {
        PodcastListFragment podcastListFragment = PodcastListFragment
                .newInstance(section);
        ((BaseActivity) getActivity()).addFragment(R.id.container_fragment,
                podcastListFragment, PodcastListFragment.TAG, true);
    }

    @Override
    public boolean handleOnBackPressed() {
        getActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().setTitle(getString(R.string.app_name));
        return true;
    }

    private void showBackArrow(boolean show) {
        setHasOptionsMenu(show);
        mActionBar.setDisplayHomeAsUpEnabled(show);
    }

    private void showHourByHour() {
        HourByHourListFragment hourByHourListFragment = HourByHourListFragment
                .newInstance((Program) getArguments().getParcelable(ARG_PROGRAM));
        ((BaseActivity) getActivity()).addFragment(R.id.container_fragment,
                hourByHourListFragment, HourByHourListFragment.TAG, true);
        mPresenter.selectedSection(false);
    }
}
