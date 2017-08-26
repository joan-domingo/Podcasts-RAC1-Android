package cat.xojan.random1.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.databinding.RecyclerViewFragmentBinding;
import cat.xojan.random1.domain.entities.CrashReporter;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;
import cat.xojan.random1.viewmodel.PodcastsViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PodcastListFragment extends BaseFragment {

    public static final String TAG = PodcastListFragment.class.getSimpleName();
    public static final String ARG_PROGRAM = "program_param";
    public static final String ARG_SECTION = "section_param";

    @Inject PodcastsViewModel mPodcastsViewModel;
    @Inject ProgramDataInteractor mProgramDataInteractor;
    @Inject CrashReporter mCrashReporter;

    private ActionBar mActionBar;
    private PodcastListAdapter mAdapter;
    private RecyclerViewFragmentBinding mBinding;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public static PodcastListFragment newInstance(Section section, Program program) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_SECTION, section);
        args.putParcelable(ARG_PROGRAM, program);

        PodcastListFragment podcastListFragment = new PodcastListFragment();
        podcastListFragment.setArguments(args);

        return podcastListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(HomeComponent.class).inject(this);
        mBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false);

        mBinding.swiperefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.swiperefresh.setOnRefreshListener(() -> showPodcasts(true));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PodcastListAdapter(getActivity(), mProgramDataInteractor);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        showPodcasts(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_export_podcasts).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
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

    @Override
    public void onResume() {
        super.onResume();
        mCompositeDisposable.add(mPodcastsViewModel.getDownloadedPodcastsUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateViewWithDownloaded));
        getActivity().setTitle(((Section) getArguments().get(ARG_SECTION)).getTitle());
        showBackArrow();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    private void showPodcasts(final boolean refresh) {
        new Handler().postDelayed(() -> {
            mBinding.swiperefresh.setRefreshing(true);
            Program program = getArguments().getParcelable(PodcastListFragment.ARG_PROGRAM);
            Section section = getArguments().getParcelable(PodcastListFragment.ARG_SECTION);

            mCompositeDisposable.add(mPodcastsViewModel.loadPodcasts(program, section, refresh)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::updateView,
                            this::handleError));
        }, 0);
    }

    private void updateView(List<Podcast> podcasts) {
        mBinding.swiperefresh.setRefreshing(false);
        mAdapter.update(podcasts);
        mBinding.emptyList.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void updateViewWithDownloaded(List<Podcast> podcasts) {
        mAdapter.updateWithDownloaded(podcasts);
    }

    private void handleError(Throwable throwable) {
        mBinding.swiperefresh.setRefreshing(false);
        mBinding.emptyList.setVisibility(View.VISIBLE);
        mCrashReporter.logException(throwable);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    private void showBackArrow() {
        setHasOptionsMenu(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
