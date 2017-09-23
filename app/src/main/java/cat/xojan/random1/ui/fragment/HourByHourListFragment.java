package cat.xojan.random1.ui.fragment;

import android.content.Context;
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
import cat.xojan.random1.injection.component.BrowseComponent;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.activity.HomeActivity;
import cat.xojan.random1.ui.adapter.PodcastListAdapter;
import cat.xojan.random1.viewmodel.PodcastsViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HourByHourListFragment extends BaseFragment {

    public static final String TAG = HourByHourListFragment.class.getSimpleName();
    public static final String ARG_PROGRAM = "program_param";

    @Inject PodcastsViewModel mPodcastsViewModel;
    @Inject ProgramDataInteractor mProgramDataInteractor;
    @Inject CrashReporter mCrashReporter;

    private RecyclerViewFragmentBinding mBinding;
    private ActionBar mActionBar;
    private PodcastListAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private HomeActivity mHomeActivity;

    public static HourByHourListFragment newInstance(Program program) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROGRAM, program);

        HourByHourListFragment hourByHourListFragment = new HourByHourListFragment();
        hourByHourListFragment.setArguments(args);

        return hourByHourListFragment;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof HomeActivity){
            mHomeActivity = (HomeActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(BrowseComponent.class).inject(this);
        mBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false);

        mBinding.swiperefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.swiperefresh.setOnRefreshListener(() -> loadPodcasts(true));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PodcastListAdapter(getActivity(), mProgramDataInteractor);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        loadPodcasts(false);
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
        mCompositeDisposable.add(mPodcastsViewModel.getDownloadedPodcastsUpdates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateViewWithDownloaded));
        showBackArrow(true);
        getActivity().setTitle(((Program) getArguments().get(ARG_PROGRAM)).getTitle());
    }

    @Override
    public void onPause() {
        super.onPause();
        showBackArrow(false);
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    @Override
    public boolean handleOnBackPressed() {
        getActivity().finish();
        return true;
    }

    private void loadPodcasts(final boolean refresh) {
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

    private void handleError(Throwable throwable) {
        mCrashReporter.logException(throwable);
        mBinding.emptyList.setVisibility(View.VISIBLE);
        mBinding.swiperefresh.setRefreshing(false);
        mBinding.recyclerView.setVisibility(View.GONE);
    }

    private void updateView(List<Podcast> podcasts) {
        mBinding.emptyList.setVisibility(View.GONE);
        mBinding.swiperefresh.setRefreshing(false);
        mAdapter.update(podcasts);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void updateViewWithDownloaded(List<Podcast> podcasts) {
        mAdapter.updateWithDownloaded(podcasts);
    }

    private void showBackArrow(boolean show) {
        setHasOptionsMenu(show);
        mActionBar.setDisplayHomeAsUpEnabled(show);
    }

    private void showSections() {
        mPodcastsViewModel.selectedSection(true);
        SectionFragment sectionListFragment = SectionFragment
                .newInstance((Program) getArguments().get(ARG_PROGRAM));
        ((BaseActivity) getActivity()).addFragment(sectionListFragment, SectionFragment.TAG, true);
    }
}
