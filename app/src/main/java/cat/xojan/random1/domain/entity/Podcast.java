package cat.xojan.random1.domain.entity;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import cat.xojan.random1.commons.ImageUtil;

@AutoValue
public abstract class Podcast implements Parcelable {

    public abstract String title();
    public abstract String description();
    public abstract String link();
    public abstract String imageUrl();
    public abstract String category();

    public static Podcast create(String title, String description, String link, String category) {
        String imageUrl = ImageUtil.getPodcastImage(category);
        return new AutoValue_Podcast(title, description, link, imageUrl, category);
    }
}
