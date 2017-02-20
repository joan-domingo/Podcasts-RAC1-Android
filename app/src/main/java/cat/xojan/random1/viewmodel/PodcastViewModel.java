package cat.xojan.random1.viewmodel;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import cat.xojan.random1.commons.EventUtil;
import cat.xojan.random1.domain.entities.Podcast;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.activity.RadioPlayerActivity;

public class PodcastViewModel {

    private final Podcast mPodcast;
    private final Context mContext;
    private final ProgramDataInteractor mProgramDataInteractor;
    private final DownloadManager mDownloadManager;

    public PodcastViewModel(Context context, Podcast podcast,
                            ProgramDataInteractor programDataInteractor,
                            DownloadManager downloadManager) {
        mPodcast = podcast;
        mContext = context;
        mProgramDataInteractor = programDataInteractor;
        mDownloadManager = downloadManager;
    }

    //TODO test
    public View.OnClickListener onClickPodcast() {
        return view -> {
            Intent intent = new Intent(mContext, RadioPlayerActivity.class);
            intent.putExtra(RadioPlayerActivity.EXTRA_PODCAST, mPodcast);
            mContext.startActivity(intent);

            EventUtil.logPlayedPodcast(mPodcast);
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

    //TODO test
    public View.OnClickListener onClickIcon() {
        return view -> {
            switch (mPodcast.getState()) {
                case LOADED:
                    download(mPodcast);
                    break;
                case DOWNLOADING:
                    break;
                case DOWNLOADED:
                    deletePodcast(mPodcast);
                    break;
            }
            mProgramDataInteractor.refreshDownloadedPodcasts();
        };
    }

    private void deletePodcast(Podcast podcast) {
        mProgramDataInteractor.deleteDownload(podcast);
    }

    private void download(Podcast podcast) {
        Uri uri = Uri.parse(podcast.getPath());
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(podcast.getTitle())
                .setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS,
                        podcast.getAudioId() + ProgramDataInteractor.EXTENSION)
                .setVisibleInDownloadsUi(true);

        mDownloadManager.enqueue(request);
        mProgramDataInteractor.addDownloadingPodcast(podcast);
    }
}
