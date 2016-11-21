package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Audio implements Parcelable {

    private String id;
    private String title;
    private Date date;
    private String duration;
    private long length;
    private Date publicationDate;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected Audio(Parcel in) {
        id = in.readString();
        title = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
        duration = in.readString();
        length = in.readLong();
        long tmpPublicationDate = in.readLong();
        publicationDate = tmpPublicationDate != -1 ? new Date(tmpPublicationDate) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeLong(date != null ? date.getTime() : -1L);
        dest.writeString(duration);
        dest.writeLong(length);
        dest.writeLong(publicationDate != null ? publicationDate.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Audio> CREATOR = new Parcelable.Creator<Audio>() {
        @Override
        public Audio createFromParcel(Parcel in) {
            return new Audio(in);
        }

        @Override
        public Audio[] newArray(int size) {
            return new Audio[size];
        }
    };
}
