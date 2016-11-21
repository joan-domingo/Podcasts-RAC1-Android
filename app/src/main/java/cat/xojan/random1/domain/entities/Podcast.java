package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Podcast implements Parcelable {

    private Audio audio;
    private String path;
    private String mFilePath;
    private Date datetime;
    private long durationSeconds;
    private String mProgramId;
    private String mImageUrl;
    private State mState;
    private String appMobileTitle;

    public Podcast(String title, State state, String filePath) {
        appMobileTitle = title;
        mState = state;
        mFilePath = filePath;
    }

    public String getTitle() {
        return appMobileTitle;
    }

    public void setProgramId(String programId) {
        mProgramId = programId;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public State getState() {
        return mState == null ? State.LOADED : mState;
    }

    public String getPath() {
        return path;
    }

    public void setState(State downloading) {
        mState = downloading;
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

    protected Podcast(Parcel in) {
        audio = (Audio) in.readValue(Audio.class.getClassLoader());
        path = in.readString();
        mFilePath = in.readString();
        long tmpDatetime = in.readLong();
        datetime = tmpDatetime != -1 ? new Date(tmpDatetime) : null;
        durationSeconds = in.readLong();
        mProgramId = in.readString();
        mImageUrl = in.readString();
        appMobileTitle = in.readString();
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
        dest.writeLong(datetime != null ? datetime.getTime() : -1L);
        dest.writeLong(durationSeconds);
        dest.writeString(mProgramId);
        dest.writeString(mImageUrl);
        dest.writeString(appMobileTitle);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Podcast> CREATOR = new Parcelable.Creator<Podcast>() {
        @Override
        public Podcast createFromParcel(Parcel in) {
            return new Podcast(in);
        }

        @Override
        public Podcast[] newArray(int size) {
            return new Podcast[size];
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

        Podcast podcast = (Podcast) o;

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
