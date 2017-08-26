package cat.xojan.random1.ui.fragment;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import cat.xojan.random1.R;
import cat.xojan.random1.databinding.RecyclerViewFragmentBinding;
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
    private RecyclerViewFragmentBinding mBinding;
    private ProgramListAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(HomeComponent.class).inject(this);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.recycler_view_fragment, container,
                false);
        mBinding.swiperefresh.setColorSchemeResources(R.color.colorAccent);
        mBinding.swiperefresh.setOnRefreshListener(() ->
                new Handler().postDelayed(this::loadPrograms, 0));
        mAdapter = new ProgramListAdapter(getContext(), mProgramDataInteractor);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
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
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
    }

    private void loadPrograms() {
        mBinding.swiperefresh.setRefreshing(true);
        mCompositeDisposable.add(mProgramsViewModel.loadPrograms()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView,
                        this::handleError));
    }

    private void updateView(List<Program> programs) {
        mBinding.swiperefresh.setRefreshing(false);
        mAdapter.updateItems(programs);
        mBinding.emptyList.setVisibility(View.GONE);
        mBinding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void handleError(Throwable e) {
        mCrashReporter.logException(e);
        mBinding.swiperefresh.setRefreshing(false);
        mBinding.emptyList.setVisibility(View.VISIBLE);
        mBinding.recyclerView.setVisibility(View.GONE);
    }
}
