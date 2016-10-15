package cat.xojan.random1.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import cat.xojan.random1.commons.ImageUtil;

public class Section implements Parcelable {

    private final String mTitle;
    private final String mParam;
    private final int mImageDrawable;

    public Section(String title, String param, String programParam) {
        mTitle = title;
        mParam = param;
        mImageDrawable = ImageUtil.getProgramImageDrawable(programParam);
    }

    public String getTitle() {
        return mTitle;
    }

    public String getParam() {
        return mParam;
    }

    public int getImageDrawable() {
        return mImageDrawable;
    }

    protected Section(Parcel in) {
        mTitle = in.readString();
        mParam = in.readString();
        mImageDrawable = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mParam);
        dest.writeInt(mImageDrawable);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Section> CREATOR = new Parcelable.Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };
}
