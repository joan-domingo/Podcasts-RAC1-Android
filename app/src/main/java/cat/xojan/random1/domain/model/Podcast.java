package cat.xojan.random1.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import cat.xojan.random1.commons.ImageUtil;

public class Podcast implements Parcelable {

    private String mDescription;
    private String mFileUrl;
    private String mImageUrl;
    private String mCategory;
    private String mFilePath;
    private State mState;
    private int mImageDrawable;

    public Podcast(String description, String fileUrl, String category) {
        mDescription = description;
        mFileUrl = fileUrl;
        mImageUrl = ImageUtil.getPodcastImageUrl(category);
        mCategory = category;
        mState = State.LOADED;
        mImageDrawable = ImageUtil.getPodcastImageDrawable(category);
    }

    public Podcast(String category, String description, String filePath, State state) {
        mCategory = category;
        mDescription = description;
        mFilePath = filePath;
        mImageUrl = ImageUtil.getPodcastImageUrl(category);
        mState = state;
        mImageDrawable = ImageUtil.getPodcastImageDrawable(category);
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

    public String getCategory() {
        return mCategory;
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

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Podcast)) {
            return false;
        }
        Podcast podcast = (Podcast) o;
        return mDescription.equals(podcast.mDescription) &&
                mCategory.equals(podcast.mCategory);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mCategory.hashCode();
        return result;
    }

    protected Podcast(Parcel in) {
        mDescription = in.readString();
        mFileUrl = in.readString();
        mImageUrl = in.readString();
        mCategory = in.readString();
        mFilePath = in.readString();
        mState = (State) in.readValue(State.class.getClassLoader());
        mImageDrawable = in.readInt();
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
        dest.writeString(mCategory);
        dest.writeString(mFilePath);
        dest.writeValue(mState);
        dest.writeInt(mImageDrawable);
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
