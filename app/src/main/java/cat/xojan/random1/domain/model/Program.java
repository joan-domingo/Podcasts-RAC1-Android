package cat.xojan.random1.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import cat.xojan.random1.commons.ImageUtil;

public class Program implements Parcelable {

    private String mCategory;
    private String mParam;
    private String mImageUrl;
    private int mImageDrawable;

    public Program (String category, String param) {
        mCategory =category;
        mImageUrl = ImageUtil.getPodcastImageUrl(category);
        mParam = param;
        mImageDrawable = ImageUtil.getPodcastImageDrawable(category);
    }

    public String getParam() {
        return mParam;
    }

    public String getCategory() {
        return mCategory;
    }

    public int getImageDrawable() {
        return mImageDrawable;
    }

    protected Program(Parcel in) {
        mCategory = in.readString();
        mParam = in.readString();
        mImageUrl = in.readString();
        mImageDrawable = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategory);
        dest.writeString(mParam);
        dest.writeString(mImageUrl);
        dest.writeInt(mImageDrawable);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Program> CREATOR = new Parcelable.Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel in) {
            return new Program(in);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };
}
