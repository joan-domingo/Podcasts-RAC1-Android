package cat.xojan.random1.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cat.xojan.random1.BuildConfig;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;
import cat.xojan.random1.ui.presenter.LatestPodcastPresenter;

public class PodcastListFragment extends BaseFragment implements
        LatestPodcastPresenter.PodcastsListener,
        PodcastListAdapter.RecyclerViewListener {

    public static final String TAG = PodcastListFragment.class.getSimpleName();
    private static final String ARG_PARAM = "program_param";

    @Inject
    LatestPodcastPresenter mPresenter;

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.empty_list) TextView mEmptyList;
    @BindView(R.id.swiperefresh) SwipeRefreshLayout mSwipeRefresh;

    private PodcastListAdapter mAdapter;
    private Unbinder unbinder;
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
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshListener());
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.destroy();
        }
        mRecyclerView.setAdapter(null);
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }

    @Override
    public void onPodcastsLoaded(List<Podcast> podcasts) {
        mPodcasts = podcasts;
        mAdapter = new PodcastListAdapter(podcasts, this);
        mRecyclerView.setAdapter(mAdapter);

        if (podcasts.isEmpty()) {
            mEmptyList.setVisibility(View.VISIBLE);
        } else {
            mEmptyList.setVisibility(View.GONE);
        }

        if (mSwipeRefresh != null) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onClick(Podcast podcast) {
        Intent intent = new Intent(getActivity(), RadioPlayerActivity.class);
        intent.putExtra(RadioPlayerActivity.EXTRA_PODCAST, podcast);
        startActivity(intent);

        logEvent(podcast);
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
        //workaround to show refreshing icon without swipe.
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                Bundle args = getArguments();
                mPodcasts = null;
                mPresenter.showPodcasts(args != null ? args.getString(ARG_PARAM) : null, mPodcasts);
            }
        });
    }

    private void logEvent(Podcast podcast) {
        if (!BuildConfig.DEBUG) {
            Answers.getInstance().logContentView(new ContentViewEvent()
                    .putContentName(podcast.category())
                    .putContentType(podcast.description()));
        }
    }

    private class SwipeRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            showPodcasts();
        }
    }
}
