package cat.xojan.random1.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cat.xojan.random1.R;
import cat.xojan.random1.commons.PicassoUtil;
import cat.xojan.random1.domain.entity.Podcast;
import cat.xojan.random1.ui.view.CircleImageView;

public class PodcastListAdapter extends RecyclerView.Adapter<PodcastListAdapter.ViewHolder> {

    private final List<Podcast> mPodcastList;
    private RecyclerViewListener mListener;

    public PodcastListAdapter(List<Podcast> podcasts, RecyclerViewListener listener) {
        mPodcastList = podcasts;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new ItemClickListener(position));
        Podcast podcast = mPodcastList.get(position);

        holder.title.setText(podcast.category());
        holder.description.setText(podcast.description());
        PicassoUtil.loadImage(holder.itemView.getContext(), podcast.imageUrl(), holder.image);
    }

    @Override
    public int getItemCount() {
        return mPodcastList.size();
    }

    public void destroy() {
        mListener = null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title) TextView title;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.circle_image) CircleImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface RecyclerViewListener {
        void onClick(Podcast podcast);
    }

    private class ItemClickListener implements View.OnClickListener {
        private final int mPosition;

        public ItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(mPodcastList.get(mPosition));
        }
    }
}
