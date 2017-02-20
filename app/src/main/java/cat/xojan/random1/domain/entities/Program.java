package cat.xojan.random1.domain.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

public class Program implements Parcelable {

    private String id;
    private String methodSelectionId;
    private String title;
    private String subtitle;
    private String description;
    private List<Section> sections;
    private int pillAdsId;
    private Images images;
    private boolean active;

    @VisibleForTesting
    public Program(String id, boolean active) {
        this.id = id;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Section> getSections() {
        return sections;
    }

    public String getImageUrl() {
        return images.getImageUrl();
    }

    public boolean isActive() {
        return active;
    }

    protected Program(Parcel in) {
        id = in.readString();
        methodSelectionId = in.readString();
        title = in.readString();
        subtitle = in.readString();
        description = in.readString();
        if (in.readByte() == 0x01) {
            sections = new ArrayList<Section>();
            in.readList(sections, Section.class.getClassLoader());
        } else {
            sections = null;
        }
        pillAdsId = in.readInt();
        active = in.readByte() != 0x00;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(methodSelectionId);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(description);
        if (sections == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(sections);
        }
        dest.writeInt(pillAdsId);
        dest.writeByte((byte) (active ? 0x01 : 0x00));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Program program = (Program) o;

        return id != null ? id.equals(program.id) : program.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @VisibleForTesting
    public void setImageUrl(String imageUrl) {
        images = new Images(imageUrl);
    }

    @VisibleForTesting
    public void setTitle(String title) {
        this.title = title;
    }

    @VisibleForTesting
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    @VisibleForTesting
    public void setIsActive(boolean isActive) {
        active = isActive;
    }
}
