package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Images implements Parcelable {

    @SerializedName("person-small")
    private String mPersonSmall;

    public String getImageUrl() {
        return mPersonSmall;
    }

    protected Images(Parcel in) {
        mPersonSmall = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPersonSmall);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Images> CREATOR = new Parcelable.Creator<Images>() {
        @Override
        public Images createFromParcel(Parcel in) {
            return new Images(in);
        }

        @Override
        public Images[] newArray(int size) {
            return new Images[size];
        }
    };
}
