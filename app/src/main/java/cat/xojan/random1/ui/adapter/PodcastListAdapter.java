package cat.xojan.random1.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import cat.xojan.random1.databinding.PodcastItemBinding;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.viewmodel.PodcastViewModel;

public class PodcastListAdapter extends RecyclerView.Adapter<PodcastListAdapter
        .PodcastItemBindingHolder> {

    private final ProgramDataInteractor mProgramDataInteractor;
    private List<Podcast> mPodcastList;
    private Context mContext;

    public PodcastListAdapter(Context context, ProgramDataInteractor programDataInteractor) {
        mPodcastList = Collections.emptyList();
        mContext = context;
        mProgramDataInteractor = programDataInteractor;
    }

    public void update(List<Podcast> podcasts) {
        mPodcastList = podcasts;
        notifyDataSetChanged();
    }

    public void updateWithDownloaded(List<Podcast> downloadedPodcasts) {
        for (Podcast podcast : mPodcastList) {
            podcast.setFilePath(null);
            podcast.setState(Podcast.State.LOADED);
        }

        for (Podcast download : downloadedPodcasts) {
            int index = mPodcastList.indexOf(download);
            if (index >= 0) {
                Podcast podcast = mPodcastList.get(index);
                podcast.setFilePath(download.getFilePath());
                podcast.setState(download.getState());
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public PodcastItemBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PodcastItemBinding podcastItemBinding = PodcastItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new PodcastItemBindingHolder(podcastItemBinding);
    }

    @Override
    public void onBindViewHolder(PodcastItemBindingHolder holder, int position) {
        Podcast podcast = mPodcastList.get(position);
        holder.binding.setViewModel(new PodcastViewModel(mContext, podcast, mProgramDataInteractor));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mPodcastList.size();
    }

    static class PodcastItemBindingHolder extends RecyclerView.ViewHolder {

        private PodcastItemBinding binding;

        PodcastItemBindingHolder(PodcastItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
