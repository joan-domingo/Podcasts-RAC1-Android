package cat.xojan.random1.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.EventUtil;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.presenter.DownloadsPresenter;
import cat.xojan.random1.presenter.PodcastListPresenter;
import cat.xojan.random1.ui.BaseActivity;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;

public class HourByHourListFragment extends BaseFragment implements
        PodcastListPresenter.PodcastsListener,
        PodcastListAdapter.RecyclerViewListener {

    public static final String TAG = HourByHourListFragment.class.getSimpleName();
    public static final String ARG_PROGRAM = "program_param";

    @Inject DownloadsPresenter mHomePresenter;
    @Inject PodcastListPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private TextView mEmptyList;
    private SwipeRefreshLayout mSwipeRefresh;
    private ActionBar mActionBar;

    private PodcastListAdapter mAdapter;

    public static HourByHourListFragment newInstance(Program program) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROGRAM, program);

        HourByHourListFragment podcastListFragment = new HourByHourListFragment();
        podcastListFragment.setArguments(args);

        return podcastListFragment;
    }

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(HomeComponent.class).inject(this);
        mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        mPresenter.setPodcastsListener(this);
        showPodcasts(false);
        getActivity().setTitle(((Program) getArguments().get(ARG_PROGRAM)).getCategory());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (((Program) getArguments().get(ARG_PROGRAM)).getSections().size() > 1) {
            inflater.inflate(R.menu.hour_by_hour, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleOnBackPressed();
                return true;
            case R.id.action_sections:
                showSections();
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
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.destroy();
        }
    }

    @Override
    public void updateRecyclerView(List<Podcast> podcasts) {
        mSwipeRefresh.setRefreshing(false);
        mAdapter = new PodcastListAdapter(podcasts, this);
        mRecyclerView.setAdapter(mAdapter);

        if (podcasts.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        } else {
            mEmptyList.setVisibility(View.GONE);
            mPresenter.refreshDownloadedPodcasts();
        }
    }

    @Override
    public void updateRecyclerViewWithDownloaded(List<Podcast> podcasts) {
        if (mAdapter != null) {
            mAdapter.updateDownloadedPodcasts(podcasts);
        }
    }

    @Override
    public void updateRecyclerView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(Podcast podcast) {
        Intent intent = new Intent(getActivity(), RadioPlayerActivity.class);
        intent.putExtra(RadioPlayerActivity.EXTRA_PODCAST, podcast);
        startActivity(intent);

        EventUtil.logPlayedPodcast(podcast);
    }

    @Override
    public void download(Podcast podcast) {
        mPresenter.download(podcast);
    }

    @Override
    public void delete(Podcast podcast) {
        mPresenter.deletePodcast(podcast);
    }

    @Override
    public boolean handleOnBackPressed() {
        getActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().setTitle(getString(R.string.app_name));
        return true;
    }

    private void showPodcasts(final boolean refresh) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                mPresenter.loadPodcasts(getArguments(), refresh);
            }
        }, 0);
    }

    private void showBackArrow(boolean show) {
        if (getArguments() != null) {
            setHasOptionsMenu(show);
            mActionBar.setDisplayHomeAsUpEnabled(show);
        }
    }

    private void showSections() {
        SectionListFragment sectionListFragment = SectionListFragment
                .newInstance((Program) getArguments().getParcelable(ARG_PROGRAM));
        ((BaseActivity) getActivity()).addFragment(R.id.container_fragment,
                sectionListFragment, SectionListFragment.TAG, true);
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            showPodcasts(true);
        }
    }
}
