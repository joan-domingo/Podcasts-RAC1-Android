package cat.xojan.random1.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import cat.xojan.random1.databinding.SectionItemBinding;
import cat.xojan.random1.domain.entities.Program;
import cat.xojan.random1.domain.entities.Section;
import cat.xojan.random1.ui.activity.BaseActivity;
import cat.xojan.random1.viewmodel.SectionViewModel;

public class SectionListAdapter extends RecyclerView.Adapter<SectionListAdapter.SectionItemViewHolder>  {

    private List<Section> mSectionList;
    private final Program mProgram;
    private Context mContext;

    public SectionListAdapter(Context context, Program program) {
        mSectionList = Collections.emptyList();
        mContext = context;
        mProgram = program;
    }

    public void updateData(List<Section> sections) {
        mSectionList = sections;
        notifyDataSetChanged();
    }

    @Override
    public SectionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SectionItemBinding sectionItemBinding = SectionItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new SectionItemViewHolder(sectionItemBinding);
    }

    @Override
    public void onBindViewHolder(SectionItemViewHolder holder, int position) {
        Section section = mSectionList.get(position);
        holder.binding.setViewModel(new SectionViewModel((BaseActivity) mContext, section, mProgram));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mSectionList.size();
    }

    static class SectionItemViewHolder extends RecyclerView.ViewHolder {

        private SectionItemBinding binding;

        SectionItemViewHolder(SectionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
