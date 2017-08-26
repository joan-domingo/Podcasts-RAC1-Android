package cat.xojan.random1.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
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
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.injection.component.HomeComponent;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.ui.adapter.SectionListAdapter;
import cat.xojan.random1.viewmodel.SectionsViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SectionFragment extends BaseFragment {

    public static final String TAG = SectionFragment.class.getSimpleName();
    private static final String ARG_PROGRAM = "program_arg";

    @Inject SectionsViewModel mSectionsViewModel;

    private RecyclerViewFragmentBinding mBinding;
    private ActionBar mActionBar;
    private SectionListAdapter mAdapter;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Program mProgram;

    public static SectionFragment newInstance(Program program) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROGRAM, program);

        SectionFragment sectionListFragment = new SectionFragment();
        sectionListFragment.setArguments(args);

        return sectionListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent(HomeComponent.class).inject(this);
        mBinding = RecyclerViewFragmentBinding.inflate(inflater, container, false);
        mProgram = (Program) getArguments().get(ARG_PROGRAM);

        mBinding.swiperefresh.setEnabled(false);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new SectionListAdapter(getActivity(), mProgram);
        mBinding.recyclerView.setAdapter(mAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sections, menu);
        menu.findItem(R.id.action_export_podcasts).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionBar = ((BaseActivity) getActivity()).getSupportActionBar();
        showBackArrow(true);
        getActivity().setTitle(mProgram.getTitle());

        loadSections();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleOnBackPressed();
                return true;
            case R.id.action_hour_by_hour:
                showHourByHour();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean handleOnBackPressed() {
        getActivity().getSupportFragmentManager()
                .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getActivity().setTitle(getString(R.string.app_name));
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(false);
        return true;
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

    private void loadSections() {
        mCompositeDisposable.add(mSectionsViewModel.loadSections(mProgram)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateView));
    }

    private void updateView(List<Section> sections) {
        mAdapter.updateData(sections);
    }

    private void showBackArrow(boolean show) {
        setHasOptionsMenu(show);
        mActionBar.setDisplayHomeAsUpEnabled(show);
    }

    private void showHourByHour() {
        mSectionsViewModel.selectedSection(false);
        HourByHourListFragment hourByHourListFragment = HourByHourListFragment
                .newInstance((Program) getArguments().get(ARG_PROGRAM));
        ((BaseActivity) getActivity()).addFragment(hourByHourListFragment,
                HourByHourListFragment.TAG, true);
    }
}
