package cat.xojan.random1.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;

public class PodcastViewModel {

    private final Podcast mPodcast;
    private final Context mContext;
    private final ProgramDataInteractor mProgramDataInteractor;

    public PodcastViewModel(Context context, Podcast podcast,
                            ProgramDataInteractor programDataInteractor) {
        mPodcast = podcast;
        mContext = context;
        mProgramDataInteractor = programDataInteractor;
    }

    public View.OnClickListener onClickPodcast() {
        return view -> {
            Intent intent = new Intent(mContext, RadioPlayerActivity.class);
            intent.putExtra(RadioPlayerActivity.EXTRA_PODCAST, mPodcast);
            mContext.startActivity(intent);
        };
    }

    public String getImageUrl() {
        return mPodcast.getImageUrl();
    }

    public String getTitle() {
        return mPodcast.getTitle();
    }

    public Podcast.State getState() {
        return mPodcast.getState();
    }

    public View.OnClickListener onClickIcon() {
        return view -> {
            switch (mPodcast.getState()) {
                case LOADED:
                    mProgramDataInteractor.download(mPodcast);
                    break;
                case DOWNLOADING:
                    break;
                case DOWNLOADED:
                    mProgramDataInteractor.deleteDownload(mPodcast);
                    break;
            }
            mProgramDataInteractor.refreshDownloadedPodcasts();
        };
    }
}
