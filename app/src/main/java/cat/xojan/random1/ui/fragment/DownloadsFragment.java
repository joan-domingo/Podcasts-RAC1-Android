package cat.xojan.random1.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.EventUtil;
import cat.xojan.random1.domain.model.Podcast;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.BaseFragment;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;
import cat.xojan.random1.presenter.DownloadsPresenter;

public class DownloadsFragment extends BaseFragment implements
        PodcastListAdapter.RecyclerViewListener,
        DownloadsPresenter.DownloadsUI {

    @Inject
    DownloadsPresenter mPresenter;

    private RecyclerView mRecyclerView;
    private TextView mEmptyList;
    private SwipeRefreshLayout mSwipeRefresh;

    private PodcastListAdapter mAdapter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getComponent(HomeComponent.class).inject(this);
        mPresenter.setUpUiListener(this);
        mPresenter.loadDownloadedPodcasts();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mEmptyList = (TextView) view.findViewById(R.id.empty_list);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefresh.setEnabled(false);
        mEmptyList.setText(getString(R.string.no_downloaded_podcasts));
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
    public void onClick(Podcast podcast) {
        Intent intent = new Intent(getActivity(), RadioPlayerActivity.class);
        intent.putExtra(RadioPlayerActivity.EXTRA_PODCAST, podcast);
        startActivity(intent);

        EventUtil.logPlayedPodcast(podcast);
    }

    @Override
    public void download(Podcast podcast) {
        // Ignore
    }

    @Override
    public void delete(Podcast podcast) {
        mPresenter.deletePodcast(podcast);
    }

    @Override
    public void updateRecyclerView(List<Podcast> podcasts) {
        if (mRecyclerView != null) {
            mAdapter = new PodcastListAdapter(podcasts, this);
            mRecyclerView.setAdapter(mAdapter);

            if (podcasts.isEmpty()) {
                mEmptyList.setVisibility(View.VISIBLE);
            } else {
                mEmptyList.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void updateRecyclerView() {
        mAdapter.notifyDataSetChanged();
    }
}
