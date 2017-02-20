package cat.xojan.random1.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import cat.xojan.random1.databinding.ProgramItemBinding;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.viewmodel.ProgramViewModel;

public class ProgramListAdapter extends
        RecyclerView.Adapter<ProgramListAdapter.ProgramItemBindingHolder>  {

    private final ProgramDataInteractor mProgramDataInteractor;
    private List<Program> mProgramList;
    private Context mContext;

    public ProgramListAdapter(Context context, ProgramDataInteractor programDataInteractor) {
        mProgramList = Collections.emptyList();
        mContext = context;
        mProgramDataInteractor = programDataInteractor;
    }

    public void updateItems(List<Program> programs) {
        mProgramList = programs;
        notifyDataSetChanged();
    }

    @Override
    public ProgramItemBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProgramItemBinding programItemBinding = ProgramItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ProgramItemBindingHolder(programItemBinding);
    }

    @Override
    public void onBindViewHolder(ProgramItemBindingHolder holder, int position) {
        Program program = mProgramList.get(position);
        holder.binding.setViewModel(
                new ProgramViewModel(mContext, program, mProgramDataInteractor));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }

    static class ProgramItemBindingHolder extends RecyclerView.ViewHolder {

        private ProgramItemBinding binding;

        ProgramItemBindingHolder(ProgramItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
