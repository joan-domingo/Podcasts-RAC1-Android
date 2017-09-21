package cat.xojan.random1.domain.entities;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import java.util.Date;

import cat.xojan.random1.BR;

public class PodcastOld extends BaseObservable implements Parcelable {

    private Audio audio;
    private String path;
    private String mFilePath;
    private Date dateTime;
    private long durationSeconds;
    private String mProgramId;
    private String mImageUrl;
    private State mState;
    private String appMobileTitle;
    private long mDownloadReference;

    @VisibleForTesting
    public PodcastOld(String path, String programId, String title) {
        this.path = path;
        mProgramId = programId;
        appMobileTitle = title;
    }

    @Bindable
    public String getTitle() {
        return appMobileTitle;
    }

    public void setProgramId(String programId) {
        mProgramId = programId;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    @Bindable
    public String getImageUrl() {
        return mImageUrl;
    }

    @Bindable
    public State getState() {
        return mState == null ? State.LOADED : mState;
    }

    public String getPath() {
        return path;
    }

    public void setState(State state) {
        mState = state;
        notifyPropertyChanged(BR.state);
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getAudioId() {
        return audio.getId();
    }

    @VisibleForTesting
    public void setAudioId(String audioId) {
        if (audio == null) {
            audio = new Audio();
        }
        audio.setId(audioId);
    }

    public Long getDownloadReference() {
        return mDownloadReference;
    }

    public void setDownloadReference(Long downloadReference) {
        mDownloadReference = downloadReference;
    }

    public Date getDate() {
        return dateTime;
    }

    protected PodcastOld(Parcel in) {
        audio = (Audio) in.readValue(Audio.class.getClassLoader());
        path = in.readString();
        mFilePath = in.readString();
        long tmpDatetime = in.readLong();
        dateTime = tmpDatetime != -1 ? new Date(tmpDatetime) : null;
        durationSeconds = in.readLong();
        mProgramId = in.readString();
        mImageUrl = in.readString();
        appMobileTitle = in.readString();
        mDownloadReference = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(audio);
        dest.writeString(path);
        dest.writeString(mFilePath);
        dest.writeLong(dateTime != null ? dateTime.getTime() : -1L);
        dest.writeLong(durationSeconds);
        dest.writeString(mProgramId);
        dest.writeString(mImageUrl);
        dest.writeString(appMobileTitle);
        dest.writeLong(mDownloadReference);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PodcastOld> CREATOR = new Parcelable.Creator<PodcastOld>
            () {
        @Override
        public PodcastOld createFromParcel(Parcel in) {
            return new PodcastOld(in);
        }

        @Override
        public PodcastOld[] newArray(int size) {
            return new PodcastOld[size];
        }
    };

    public enum State {
        LOADED,
        DOWNLOADING,
        DOWNLOADED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PodcastOld podcast = (PodcastOld) o;

        if (!path.equals(podcast.path)) return false;
        if (!mProgramId.equals(podcast.mProgramId)) return false;
        return appMobileTitle != null ? appMobileTitle.equals(podcast.appMobileTitle) : podcast.appMobileTitle == null;

    }

    @Override
    public int hashCode() {
        int result = path.hashCode();
        result = 31 * result + mProgramId.hashCode();
        result = 31 * result + (appMobileTitle != null ? appMobileTitle.hashCode() : 0);
        return result;
    }
}
