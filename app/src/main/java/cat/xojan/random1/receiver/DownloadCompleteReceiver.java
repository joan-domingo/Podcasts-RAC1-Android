package cat.xojan.random1.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import javax.inject.Inject;

import cat.xojan.random1.Application;
import cat.xojan.random1.R;
import cat.xojan.random1.commons.ErrorUtil;
import cat.xojan.random1.commons.EventUtil;
import cat.xojan.random1.domain.interactor.PodcastDataInteractor;

public class DownloadCompleteReceiver extends BroadcastReceiver {

    private static final String TAG = DownloadCompleteReceiver.class.getSimpleName();

    @Inject PodcastDataInteractor mPodcastDataInteractor;
    @Inject DownloadManager mDownloadManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive()");
        initInjector(context);

        long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(reference);
        Cursor cursor = mDownloadManager.query(query);

        if (cursor != null && cursor.moveToFirst()) {
            // get status of the download
            int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(statusIndex);

            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(context,
                            context.getString(R.string.download_successful),
                            Toast.LENGTH_SHORT).show();

                    // get title of the download
                    int categoryIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE);
                    String category = cursor.getString(categoryIndex);

                    // get description of the download
                    int descriptionIndex =
                            cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION);
                    String description = cursor.getString(descriptionIndex);

                    EventUtil.logDownloaedPodcast(category, description);
                    mPodcastDataInteractor.addDownload(category, description);
                    break;

                case DownloadManager.STATUS_FAILED:
                    // get fail reason
                    int reasonIndex = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
                    int reason = cursor.getInt(reasonIndex);
                    String reasonText = null;

                    switch(reason){
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            reasonText = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            reasonText = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            reasonText = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            reasonText = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            reasonText = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            reasonText = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            reasonText = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            reasonText = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            reasonText = "ERROR_UNKNOWN";
                            break;
                    }

                    ErrorUtil.logException("Download failed: " + reason + " " + reasonText);
                    Toast.makeText(context,
                            context.getString(R.string.download_failed),
                            Toast.LENGTH_SHORT).show();
                    mPodcastDataInteractor.refreshDownloadedPodcasts();
                    break;
            }
            cursor.close();
        } else {
            Toast.makeText(context, context.getString(R.string.download_cancelled),
                    Toast.LENGTH_SHORT).show();
            mPodcastDataInteractor.refreshDownloadedPodcasts();
        }
    }

    private void initInjector(Context context) {
        ((Application) context.getApplicationContext()).getAppComponent().inject(this);
    }
}