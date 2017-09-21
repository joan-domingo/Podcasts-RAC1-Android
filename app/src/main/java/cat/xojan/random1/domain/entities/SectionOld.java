package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

public class SectionOld implements Parcelable {

    private String id;
    private String title;
    private String mImageUrl;
    private boolean active;
    private SectionType type;

    @VisibleForTesting
    public SectionOld(String id, boolean isActive, SectionType type) {
        this.id = id;
        this.active = isActive;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public SectionType getType() {
        return type;
    }

    protected SectionOld(Parcel in) {
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
    public static final Parcelable.Creator<SectionOld> CREATOR = new Parcelable.Creator<SectionOld>() {
        @Override
        public SectionOld createFromParcel(Parcel in) {
            return new SectionOld(in);
        }

        @Override
        public SectionOld[] newArray(int size) {
            return new SectionOld[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SectionOld section = (SectionOld) o;

        return id.equals(section.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
