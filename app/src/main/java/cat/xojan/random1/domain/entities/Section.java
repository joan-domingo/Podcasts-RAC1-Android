package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Section implements Parcelable {

    private String id;
    private String title;
    private String mImageUrl;
    private boolean active;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public boolean isActive() {
        return active;
    }

    protected Section(Parcel in) {
        id = in.readString();
        title = in.readString();
        mImageUrl = in.readString();
        active = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(mImageUrl);
        dest.writeByte((byte) (active ? 0x01 : 0x00));
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
