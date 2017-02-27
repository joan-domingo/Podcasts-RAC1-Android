package cat.xojan.random1.domain.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PodcastData {

    @SerializedName("result")
    private List<Podcast> podcasts;

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}
