package cat.xojan.random1.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.domain.entity.Program;

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.ViewHolder>  {

    private final List<Program> mProgramList;
    private RecyclerViewListener mListener;

    public ProgramListAdapter(List<Program> programs, RecyclerViewListener listener) {
        mProgramList = programs;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new ItemClickListener(position));
        Program program = mProgramList.get(position);

        holder.title.setText(program.category());
        Picasso.with(holder.itemView.getContext())
                .load(program.imageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mProgramList.size();
    }

    public void destroy() {
        mListener = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.circle_image) ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecyclerViewListener {
        void onClick(Program program);
    }

    private class ItemClickListener implements View.OnClickListener {
        private final int mPosition;

        public ItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(mProgramList.get(mPosition));
        }
    }
}
