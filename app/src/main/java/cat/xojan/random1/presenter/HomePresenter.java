package cat.xojan.random1.presenter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import cat.xojan.random1.R;
import cat.xojan.random1.domain.interactor.ProgramDataInteractor;
import cat.xojan.random1.ui.BasePresenter;

public class HomePresenter implements BasePresenter {

    private final Context mContext;
    private final ProgramDataInteractor mProgramDataInteractor;

    public HomePresenter(Context context, ProgramDataInteractor programDataInteractor) {
        mContext = context;
        mProgramDataInteractor = programDataInteractor;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void exportPodcasts() {
        File iternalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PODCASTS);
        File externalFilesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PODCASTS);

        externalFilesDir.mkdirs();

        for (File podcastFile : iternalFileDir.listFiles()) {
            String audioId = podcastFile.getPath()
                    .split(Environment.DIRECTORY_PODCASTS + "/")[1].replace(".mp3", "");

            String podcastTitle = mProgramDataInteractor.getDownloadedPodcastTitle(audioId);

            if (!TextUtils.isEmpty(podcastTitle)) {
                podcastTitle = podcastTitle.replace("/", "-");
                File dest = new File(externalFilesDir, podcastTitle + ".mp3");
                copy(podcastFile, dest);
            }
        }
        Toast.makeText(mContext, mContext.getString(R.string.podcasts_exported), Toast.LENGTH_SHORT)
                .show();
    }

    private void copy(File src, File dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
