package cat.xojan.random1.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import cat.xojan.random1.commons.ImageUtil;

public class Podcast implements Parcelable {

    private String mDescription;
    private String mFileUrl;
    private String mImageUrl;
    private String mProgram;
    private String mFilePath;
    private State mState;
    private int mImageDrawable;
    private String mProgramTitle;

    public Podcast(String description, String fileUrl, String program, String programTitle) {
        mDescription = description;
        mFileUrl = fileUrl;
        mImageUrl = ImageUtil.getPodcastImageUrl(program);
        mProgram = program;
        mState = State.LOADED;
        mImageDrawable = ImageUtil.getPodcastImageDrawable(program, programTitle);
        mProgramTitle = programTitle;
    }

    public Podcast(String program, String description, String filePath, State state,
                   String programTitle) {
        mProgram = program;
        mDescription = description;
        mFilePath = filePath;
        mImageUrl = ImageUtil.getPodcastImageUrl(program);
        mState = state;
        mImageDrawable = ImageUtil.getPodcastImageDrawable(program, programTitle);
    }

    public String getDescription() {
        return mDescription;
    }

    public String getFileUrl() {
        return mFileUrl;
    }

    public int getImageDrawable() {
        return mImageDrawable;
    }

    public String getProgram() {
        return mProgram;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public State getState() {
        return mState;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public void setState(State state) {
        mState = state;
    }

    public String getProgramTitle() {
        return mProgramTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Podcast)) {
            return false;
        }
        Podcast podcast = (Podcast) o;
        return mDescription.equals(podcast.mDescription) &&
                mProgram.equals(podcast.mProgram);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mProgram.hashCode();
        return result;
    }

    protected Podcast(Parcel in) {
        mDescription = in.readString();
        mFileUrl = in.readString();
        mImageUrl = in.readString();
        mProgram = in.readString();
        mFilePath = in.readString();
        mState = (State) in.readValue(State.class.getClassLoader());
        mImageDrawable = in.readInt();
        mProgramTitle = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDescription);
        dest.writeString(mFileUrl);
        dest.writeString(mImageUrl);
        dest.writeString(mProgram);
        dest.writeString(mFilePath);
        dest.writeValue(mState);
        dest.writeInt(mImageDrawable);
        dest.writeString(mProgramTitle);
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
}
