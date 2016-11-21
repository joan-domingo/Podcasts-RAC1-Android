package cat.xojan.random1.domain.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PodcastData {

    @SerializedName("result")
    private List<Podcast> podcasts;

    private String success;

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public String getSuccess() {
        return success;
    }
}
