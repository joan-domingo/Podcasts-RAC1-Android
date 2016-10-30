package cat.xojan.random1.ui.adapter;

import android.graphics.drawable.Animatable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cat.xojan.random1.R;
import cat.xojan.random1.commons.PicassoUtil;
import cat.xojan.random1.commons.PodcastUtil;
import cat.xojan.random1.domain.model.Podcast;

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

        holder.title.setText(podcast.getProgram());
        holder.description.setText(podcast.getDescription());
        PicassoUtil.loadImage(holder.itemView.getContext(), podcast.getImageDrawable(),
                holder.image, true);

        switch (podcast.getState()) {
            case LOADED:
                holder.icon.setImageResource(R.drawable.ic_arrow_down);
                break;
            case DOWNLOADING:
                holder.icon.setImageResource(R.drawable.animated_arrow);
                if (holder.icon.getDrawable() instanceof Animatable) {
                    ((Animatable) holder.icon.getDrawable()).start();
                }
                break;
            case DOWNLOADED:
                holder.icon.setImageResource(R.drawable.ic_delete);
                break;
        }
        holder.icon.setOnClickListener(new IconClickListener(position));
    }

    @Override
    public int getItemCount() {
        return mPodcastList.size();
    }

    public void destroy() {
        mListener = null;
    }

    public void updateDownloadedPodcasts(List<Podcast> downloadedPodcasts) {
        PodcastUtil.updateDownloadedPodcasts(mPodcastList, downloadedPodcasts);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageView image;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            image = (ImageView) itemView.findViewById(R.id.circle_image);
            icon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    public interface RecyclerViewListener {
        /** Podcast item was clicked */
        void onClick(Podcast podcast);
        /** Download icon was clicked*/
        void download(Podcast podcast);
        /** Delete icon was clicked*/
        void delete(Podcast podcast);
    }

    private class ItemClickListener implements View.OnClickListener {
        private final int mPosition;

        ItemClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(mPodcastList.get(mPosition));
        }
    }

    private class IconClickListener implements View.OnClickListener {
        private final int mPosition;

        IconClickListener(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            Podcast podcast = mPodcastList.get(mPosition);
            switch (podcast.getState()) {
                case LOADED:
                    mListener.download(podcast);
                    break;

                case DOWNLOADING:
                    break;

                case DOWNLOADED:
                    mListener.delete(podcast);
                    break;
            }
        }
    }
}
