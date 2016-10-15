package cat.xojan.random1.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cat.xojan.random1.commons.ImageUtil;

public class Program implements Parcelable {

    private List<Section> mSections;
    private String mTitle;
    private String mParam;
    private String mImageUrl;
    private int mImageDrawable;

    public Program(String title, String param, List<Section> sections) {
        mTitle = title;
        mImageUrl = ImageUtil.getPodcastImageUrl(param);
        mParam = param;
        mImageDrawable = ImageUtil.getProgramImageDrawable(param);
        mSections = sections;
    }

    public String getParam() {
        return mParam;
    }

    public String getCategory() {
        return mTitle;
    }

    public int getImageDrawable() {
        return mImageDrawable;
    }

    public List<Section> getSections() {
        return mSections;
    }

    protected Program(Parcel in) {
        if (in.readByte() == 0x01) {
            mSections = new ArrayList<Section>();
            in.readList(mSections, Section.class.getClassLoader());
        } else {
            mSections = null;
        }
        mTitle = in.readString();
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
        if (mSections == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSections);
        }
        dest.writeString(mTitle);
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
