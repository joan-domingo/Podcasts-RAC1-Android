package cat.xojan.random1.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.Log;
import cat.xojan.random1.databinding.RecyclerViewFragmentBinding;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;
import cat.xojan.random1.viewmodel.PodcastsViewModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DownloadsFragment extends BaseFragment {

    @Inject PodcastsViewModel mPodcastsViewModel;
    @Inject ProgramDataInteractor mProgramDataInteractor;

    private CompositeSubscription mSubscription = new CompositeSubscription();
    private PodcastListAdapter mAdapter;
    private RecyclerViewFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(HomeComponent.class).inject(this);
        mBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false);

        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.swiperefresh.setEnabled(false);
        mBinding.emptyList.setText(getString(R.string.no_downloaded_podcasts));

        mAdapter = new PodcastListAdapter(getContext(), mProgramDataInteractor);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscription.add(mPodcastsViewModel.loadDownloadedPodcasts()
                .subscribeOn(Schedulers.io())
                /*.flatMap(Observable::from)
                .filter(podcast -> podcast.getState().equals(Podcast.State.DOWNLOADED))
                .toSortedList((podcast, podcast2) -> podcast2.getDate().compareTo(podcast.getDate()))*/
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView));
        mSubscription.add(mPodcastsViewModel.getDownloadedPodcastsUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView));
    }

    @Override
    public void onPause() {
        super.onPause();
        mSubscription.clear();
    }

    private void updateView(List<Podcast> podcasts) {
        List<Podcast> downloaded = new ArrayList<>();
        if (podcasts.isEmpty()) {
            mBinding.emptyList.setVisibility(View.VISIBLE);
        } else {
            for (Podcast p : podcasts) {
                if (p.getState().equals(Podcast.State.DOWNLOADED)) {
                    downloaded.add(p);
                }
            }
            Collections.sort(downloaded, (podcast, podcast2) -> podcast2.getDate().compareTo(podcast.getDate()));
            mBinding.emptyList.setVisibility(View.GONE);
        }

        mAdapter.update(downloaded);
    }
}
