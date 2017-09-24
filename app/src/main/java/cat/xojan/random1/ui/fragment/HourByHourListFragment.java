package cat.xojan.random1.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
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

    private PodcastListAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private HomeActivity mHomeActivity;

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mEmptyList;

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
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        setHasOptionsMenu(true);

        mSwipeRefresh = view.findViewById(R.id.swipe_refresh);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyList = view.findViewById(R.id.empty_list);

        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(() -> loadPodcasts(true));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new PodcastListAdapter(mProgramDataInteractor);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            mSwipeRefresh.setRefreshing(true);
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
        mEmptyList.setVisibility(View.VISIBLE);
        mSwipeRefresh.setRefreshing(false);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void updateView(List<Podcast> podcasts) {
        mEmptyList.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
        mAdapter.setPodcasts(podcasts);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void updateViewWithDownloaded(List<Podcast> podcasts) {
        mAdapter.updateWithDownloaded(podcasts);
    }

    private void showSections() {
        mPodcastsViewModel.selectedSection(true);
        SectionFragment sectionListFragment = SectionFragment
                .newInstance((Program) getArguments().get(ARG_PROGRAM));
        ((BaseActivity) getActivity()).addFragment(sectionListFragment, SectionFragment.TAG, true);
    }
}
