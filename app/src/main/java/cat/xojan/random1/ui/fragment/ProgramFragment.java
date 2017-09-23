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
import cat.xojan.random1.domain.entities.CrashReporter;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.adapter.ProgramListAdapter;
import cat.xojan.random1.viewmodel.ProgramsViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ProgramFragment extends BaseFragment {

    @Inject ProgramsViewModel mProgramsViewModel;
    @Inject ProgramDataInteractor mProgramDataInteractor;
    @Inject CrashReporter mCrashReporter;

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private ProgramListAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefresh;
    private RecyclerView mRecyclerView;
    private TextView mEmptyList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(HomeComponent.class).inject(this);
        View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        mSwipeRefresh = view.findViewById(R.id.swiperefresh);
        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefresh.setOnRefreshListener(() ->
                new Handler().postDelayed(this::loadPrograms, 0));
        mAdapter = new ProgramListAdapter( mProgramDataInteractor);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mEmptyList = view.findViewById(R.id.empty_list);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setLayoutManager(getResources().getConfiguration().orientation);
        loadPrograms();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }

    private void setLayoutManager(int orientation) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }

    private void loadPrograms() {
        mSwipeRefresh.setRefreshing(true);
        mCompositeDisposable.add(mProgramsViewModel.loadPrograms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView,
                        this::handleError));
    }

    private void updateView(List<Program> programs) {
        mSwipeRefresh.setRefreshing(false);
        mAdapter.setPrograms(programs);
        mEmptyList.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void handleError(Throwable e) {
        mCrashReporter.logException(e);
        mSwipeRefresh.setRefreshing(false);
        mEmptyList.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }
}
