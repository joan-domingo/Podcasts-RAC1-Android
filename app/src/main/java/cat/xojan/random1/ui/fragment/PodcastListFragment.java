package cat.xojan.random1.ui.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.EventUtil;
import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.presenter.DownloadsPresenter;
import cat.xojan.random1.presenter.PodcastListPresenter;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;

public class PodcastListFragment extends BaseFragment implements
        PodcastListPresenter.PodcastsListener,
        PodcastListAdapter.RecyclerViewListener {

    public static final String TAG = PodcastListFragment.class.getSimpleName();
    private static final String ARG_PARAM = "program_param";

    @Inject
    DownloadsPresenter mHomePresenter;
    @Inject
    PodcastListPresenter mPresenter;

    RecyclerView mRecyclerView;
    TextView mEmptyList;
    SwipeRefreshLayout mSwipeRefresh;

    private PodcastListAdapter mAdapter;
    private List<Podcast> mPodcasts;

    public static PodcastListFragment newInstance(String param) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);

        PodcastListFragment podcastListFragment = new PodcastListFragment();
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
        mPresenter.setPodcastsListener(this);
        showPodcasts();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void updateRecyclerView(List<Podcast> podcasts) {
        mPodcasts = podcasts;
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
        mAdapter.updateDownloadedPodcasts(podcasts);
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPodcasts() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                Bundle args = getArguments();
                mPodcasts = null;
                mPresenter.loadPodcasts(args != null ? args.getString(ARG_PARAM) : null, mPodcasts);
            }
        }, 0);
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            showPodcasts();
        }
    }
}
