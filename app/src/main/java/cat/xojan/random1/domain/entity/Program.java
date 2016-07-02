package cat.xojan.random1.domain.entity;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import cat.xojan.random1.commons.ImageUtil;

@AutoValue
public abstract class Program implements Parcelable {

    public abstract String category();
    public abstract String param();
    public abstract String imageUrl();

    public static Program create(String category, String param) {
        String imageUrl = ImageUtil.getPodcastImage(category);
        return new AutoValue_Program(category, param, imageUrl);
    }
}
